package user;

import ManagmentDB.MySQLConnector;
import auth.UserObject;
import com.jcraft.jsch.JSchException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserObject> signIn(@RequestBody UserObject user, HttpSession session) {
        String email = user.getEmail();
        String password = user.getPassword();

        boolean isValidCredentials = MySQLConnector.checkCredentials(email, password);
        String logMessage = "Sign-in request received for email: " + email;
        if (!isValidCredentials) {
            logMessage +=", authentication failed";
            log.warn(logMessage);
            UserObject response = new UserObject();
            response.setMessage("Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        logMessage += ", authentication successful";
        log.info(logMessage);

        // Check if the user has a session ID in the database
        try (Connection conn = MySQLConnector.getConnection()) {
            String sql = "SELECT * FROM sessions WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Update the session ID in the database
                String sessionId = session.getId();
                String updateSql = "UPDATE sessions SET SessionId = ? WHERE Email = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, sessionId);
                updateStmt.setString(2, email);
                updateStmt.executeUpdate();

                UserObject response = new UserObject();
                response.setEmail(email);
                response.setSessionId(sessionId);
                response.setMessage("Login successful");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
        // Insert a new row for the user's session in the database
        String sessionId = session.getId();
        session.setAttribute("user", user);
        session.setAttribute("sessionId", sessionId);
        String tableName = "sessions";
        String[] columnNames = {"Email", "SessionId"};
        String[] values = {email, sessionId};
        MySQLConnector.insertNewRow(tableName, columnNames, values);

        UserObject response = new UserObject();
        response.setEmail(email);
        response.setSessionId(sessionId);
        response.setMessage("Login successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getUserInfo")
    public ResponseEntity<UserObject> getUserInfo(@RequestParam("sessionID") String sessionID) {
        UserObject newUser = new UserObject();
        String logMessage = "Get user info request received for session ID: " + sessionID;

        // Check if the session ID exists in the database
        try (Connection conn = MySQLConnector.getConnection()) {
            String tableName = "sessions";
            String columnName = "SessionId";
            String whereValue = sessionID;
            List<Map<String, Object>> resultList = MySQLConnector.select(tableName, columnName, whereValue);
            for (Map<String, Object> row : resultList) {
                Object sessionId = row.get("SessionId");
                Object email = row.get("Email");
                if (sessionId.equals(sessionID)) {
                    // Retrieve the user's first name and last name based on email
                    String firstName = userService.getUserFirstNameFromEmail(conn, email.toString());
                    String lastName = userService.getUserLastNameFromEmail(conn, email.toString());
                    String dogName = userService.getDogNameFromEmail(conn, email.toString());
                    String dogWeight = userService.getDogWeightFromEmail(conn,email.toString());
                    String dogAge = userService.getDogAgeFromEmail(conn,email.toString());
                    if (firstName != null && lastName != null) {
                        logMessage += ", user info retrieved successfully";
                        log.info(logMessage);
                        newUser.setSessionId(sessionId.toString());
                        newUser.setEmail(email.toString());
                        newUser.setFirstName(firstName);
                        newUser.setLastName(lastName);
                        if(dogName != null ) newUser.setDogName(dogName);
                        if(dogWeight != null ) newUser.setDogWeight(Integer.valueOf(dogWeight));
                        if(dogAge != null ) newUser.setDogAge(Integer.valueOf(dogAge));
                        return new ResponseEntity<>(newUser, HttpStatus.OK);
                    }
                }
            }
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }

        logMessage += ", session ID not found or user not logged in";
        log.warn(logMessage);
        UserObject response = new UserObject();
        response.setMessage("Session ID not found or user not logged in");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    private String getUserFullNameFromEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT FullName FROM users WHERE Email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("FullName");
        }
        return null;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String dogName,
            @RequestParam(required = false) String dogWeight,
            @RequestParam(required = false) Integer dogAge) {
        String logMessage = "Sign-up request received for email: " + email;
        if (MySQLConnector.checkIfUserExists(email)) {
            logMessage += ", user already exists";
            log.warn(logMessage);
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        String tableName = "users";
        String[] columnNames = {"First_Name", "Last_Name", "Email", "Password"};
        String[] values = {firstName, lastName, email, password};
        if (dogName != null) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            values = Arrays.copyOf(values, values.length + 1);
            columnNames[columnNames.length - 1] = "Dog_Name";
            values[values.length - 1] = dogName;
        }
        if (dogWeight != null) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            values = Arrays.copyOf(values, values.length + 1);
            columnNames[columnNames.length - 1] = "Dog_Weight";
            values[values.length - 1] = dogWeight;
        }

        if (dogAge != null) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            values = Arrays.copyOf(values, values.length + 1);
            columnNames[columnNames.length - 1] = "Dog_Age";
            values[values.length - 1] = String.valueOf(dogAge);
        }
        MySQLConnector.insertNewRow(tableName, columnNames, values);
        logMessage += ", user created successfully";
        log.info(logMessage);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }
}
