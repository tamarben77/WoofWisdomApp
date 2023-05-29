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
import vaccinations.NextVaccinations;
import DTO.VaccinationDetails;
import vaccinations.VaccinationsManager;
import DogFoodRequests.DogFoodQuery;
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
        System.out.println("got an add to calendar request...");
        Gson gson = new Gson();
        GoogleCalendarEvent Event = gson.fromJson(googleCalendarEvent, GoogleCalendarEvent.class);
        AddEventToGoogleCalendar add = new AddEventToGoogleCalendar(Event);
        add.AddEvent();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/getNearestVet")
    public ResponseEntity GetNearestVet(@RequestBody String client_location) throws Exception {
        System.out.println("Got a request to find nearest vets...");
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

    @GetMapping ("/showDogFoodCategories")
    public ResponseEntity showDogFoodCategories() throws SQLException, JsonProcessingException {
        return DogFoodQuery.showDogFoodCategories();
    }

    @GetMapping ("/showDogFoodItemsByCategory")
    public ResponseEntity showDogFoodItemsByCategory(@RequestParam String category_name) throws SQLException, JsonProcessingException, JSchException {
        return DogFoodQuery.showDogFoodItemsByCategory(category_name);
    }

    @PostMapping("/addVaccination")
    public ResponseEntity AddVaccination(@RequestBody String vaccination_details) throws SQLException, JsonProcessingException {
        System.out.println("got a request to add vaccination...");
        Gson gson = new Gson();
        VaccinationDetails vaccinationDetails = gson.fromJson(vaccination_details, VaccinationDetails.class);
        VaccinationsManager.addNewVaccinationRecord(vaccinationDetails.getUsername(), vaccinationDetails.getVaccination_name(),
                vaccinationDetails.getDate(), vaccinationDetails.getDescription(), vaccinationDetails.getLocation());
        return VaccinationsManager.showAllVaccinations();
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

    @GetMapping("/next-vaccinations")
    @ResponseBody
    public ResponseEntity<String> getNextVaccinations(@RequestParam("dogAgeInWeeks") int dogAgeInWeeks) throws JsonProcessingException {
        System.out.println("got a request for next vaccinations recommendation...");
        List<String> allVaccinations = NextVaccinations.getNextVaccinations(dogAgeInWeeks);
        StringBuilder res = new StringBuilder();
        for(String vaccination: allVaccinations){
            res.append(vaccination).append("\n");
        }
        System.out.println(res.toString());
        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }

    @GetMapping("/neta")
    @ResponseBody
    public ResponseEntity<String> neta() {
        return new ResponseEntity<>("Hiiii", HttpStatus.OK);
    }
}
