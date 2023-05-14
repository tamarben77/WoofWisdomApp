package WoofWisdom;

import AddToCalender.AddEventToGoogleCalendar;
import AddToCalender.GoogleCalendarEvent;
import ManagmentDB.MySQLConnector;
import SearchGoogleMaps.ClientLocation;
import SearchGoogleMaps.VetFinder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vaccinations.VaccinationDetails;
import vaccinations.VaccinationsManager;
import auth.UserObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class WoofWisdomController {
    private static final Logger log = LoggerFactory.getLogger(WoofWisdomController.class);
    @PostMapping(value = "/addToCalender")
    public ResponseEntity AddToGoogleCalender(@RequestBody String googleCalendarEvent) throws IOException, GeneralSecurityException {
        Gson gson = new Gson();
        GoogleCalendarEvent Event = gson.fromJson(googleCalendarEvent, GoogleCalendarEvent.class);
        AddEventToGoogleCalendar add = new AddEventToGoogleCalendar(Event);
        add.AddEvent();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/getNearestVet")
    public ResponseEntity GetNearestVet(@RequestBody String client_location) throws Exception {
        Gson gson = new Gson();
        ClientLocation clientLocation = gson.fromJson(client_location, ClientLocation.class);
        String response = VetFinder.getVetLocations(Double.valueOf(clientLocation.getClient_latitude()), Double.valueOf(clientLocation.getClient_longitude()));
        ResponseEntity res = new ResponseEntity<>(response, HttpStatus.OK);
        return res;
    }

    @GetMapping ("/showVaccinations")
    public ResponseEntity showVaccinations() throws SQLException, JsonProcessingException {
        return VaccinationsManager.showAllVaccinations();
    }

    @PostMapping("/addVaccination")
    public ResponseEntity AddVaccination(@RequestBody String vaccination_details) throws SQLException, JsonProcessingException {
        Gson gson = new Gson();
        VaccinationDetails vaccinationDetails = gson.fromJson(vaccination_details, VaccinationDetails.class);
        VaccinationsManager.addNewVaccinationRecord(vaccinationDetails.getUsername(), vaccinationDetails.getVaccination_name(),
                vaccinationDetails.getDate(), vaccinationDetails.getDescription(), vaccinationDetails.getLocation());
        return VaccinationsManager.showAllVaccinations();
    }

/*    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody String userCredentials) {
        Gson gson = new Gson();
        UserManagement credentials = gson.fromJson(userCredentials, UserManagement.class);

        // Validate user credentials and authenticate user
        boolean isValidCredentials = UserManagement.validateCredentials(credentials.getUsername(), credentials.getPassword());
        if (!isValidCredentials) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        User user = UserManagement.authenticateUser(credentials.getUsername(), credentials.getPassword());
        if (user == null) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }

        // Create and return response
       //String token = generateAuthToken(user);
       // SignInResponse response = new SignInResponse(user.getId(), token);
        String response = "Login successfully";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/

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
    public ResponseEntity<UserObject> getUserInfo(@RequestBody UserObject user) {
        UserObject newUser = new UserObject();
        String logMessage = "Get user info request received for session ID: " + user.getSessionID();

        // Check if the session ID exists in the database
        try (Connection conn = MySQLConnector.getConnection()) {
            String tableName = "sessions";
            String columnName = "SessionId" ;
            String whereValue  = user.getSessionID();
            List<Map<String, Object>> resultList = MySQLConnector.select(tableName, columnName, whereValue);
            for (Map<String, Object> row : resultList) {
                Object sessionId = row.get("SessionId");
                Object email = row.get("Email");
                if (sessionId.equals(user.getSessionID())) {
                    logMessage += ", user info retrieved successfully";
                    log.info(logMessage);
                    newUser.setSessionId(sessionId.toString());
                    newUser.setEmail(email.toString());
                    return new ResponseEntity<>(newUser, HttpStatus.OK);
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

    @GetMapping("/neta")
    @ResponseBody
    public void neta() {
        String tableName = "users";
        String[] columnNames = {"First_Name", "Last_Name", "Email", "Password"};
        String[] values = {"John", "Doe", "johndoe@example.com", "password123"};
        MySQLConnector.insertNewRow(tableName, columnNames, values);
        /*return "hiiiiii";*/
    }
}
