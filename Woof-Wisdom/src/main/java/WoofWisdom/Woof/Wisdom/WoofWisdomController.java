package WoofWisdom.Woof.Wisdom;

import AddToCalender.AddEventToGoogleCalendar;
import AddToCalender.GoogleCalendarEvent;
import SearchGoogleMaps.ClientLocation;
import SearchGoogleMaps.VetFinder;
import com.google.gson.Gson;
import com.google.maps.errors.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class WoofWisdomController {
    @PostMapping(value = "/addToCalender")
    public ResponseEntity AddToGoogleCalender(@RequestBody String googleCalendarEvent) throws IOException, GeneralSecurityException {
        Gson gson = new Gson();
        GoogleCalendarEvent Event = gson.fromJson(googleCalendarEvent, GoogleCalendarEvent.class);
        AddEventToGoogleCalendar add = new AddEventToGoogleCalendar(Event);
        add.AddEvent();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/getNearestVet")
    public ResponseEntity GetNearestVet(@RequestBody String client_location) throws InterruptedException, ApiException, IOException {
        Gson gson = new Gson();
        ClientLocation clientLocation = gson.fromJson(client_location, ClientLocation.class);
        String response = VetFinder.getVetLocations(Double.valueOf(clientLocation.getClient_latitude()), Double.valueOf(clientLocation.getClient_longitude()));
        ResponseEntity res = new ResponseEntity<>(response, HttpStatus.OK);
        return res;
    }

    @GetMapping("/neta")
    @ResponseBody
    public String neta() {
        return "hiiiiii";
    }
}
