package WoofWisdom.Woof.Wisdom;

import AddToCalender.AddEventToGoogleCalendar;
import AddToCalender.GoogleCalendarEvent;
import ManagmentDB.MySQLConnector;
import SearchGoogleMaps.ClientLocation;
import SearchGoogleMaps.VetFinder;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import woofWisdom.auth.UserObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

@Controller
public class WoofWisdomController {
    private static final Logger log = LoggerFactory.getLogger(WoofWisdomController.class);
    @PostMapping(value = "/addToCalender")
    public ResponseEntity AddToGoogleCalender(@RequestBody String googleCalendarEvent) throws IOException, GeneralSecurityException, ParseException {
        Gson gson = new Gson();
        GoogleCalendarEvent Event = gson.fromJson(googleCalendarEvent, GoogleCalendarEvent.class);
        AddEventToGoogleCalendar add = new AddEventToGoogleCalendar();
        AddEventToGoogleCalendar.addEventToGoogleCalendar(Event.getSummary(), Event.getDescription(), Event.getLocation(), Event.getStartDate(), Event.getEndDate(), Event.getUserEmail());
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
    public ResponseEntity<UserObject> signIn(@RequestBody UserObject user) {
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
        UserObject response = new UserObject();
        response.setMessage("Login successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {
        String logMessage = "Sign-up request received for email: " + email;
        if (MySQLConnector.checkIfUserExists(email)) {
            logMessage += ", user already exists";
            log.warn(logMessage);
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        String tableName = "users";
        String[] columnNames = {"First_Name", "Last_Name", "Email", "Password"};
        String[] values = {firstName, lastName, email, password};
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
