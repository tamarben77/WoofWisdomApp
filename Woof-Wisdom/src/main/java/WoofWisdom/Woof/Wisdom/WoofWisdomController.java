package WoofWisdom.Woof.Wisdom;
import AddToCalender.AddEventToGoogleCalendar;
import AddToCalender.GoogleCalendarEvent;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
