package AddToCalender;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

    public class AddEventToGoogleCalendar {
    private static final String APPLICATION_NAME = "Woof Wisdom App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/client_secret.json";
    private static GoogleCalendarEvent eventToAdd;

    public AddEventToGoogleCalendar(GoogleCalendarEvent Event){
        eventToAdd = Event;
    }

    public void AddEvent() throws IOException, GeneralSecurityException {
            // Build the credentials
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH)));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR))
                    .build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8888).build()).authorize(eventToAdd.getUserId());

            // Build the Calendar service
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Create a new event
            Event event = new Event()
                    .setSummary(eventToAdd.getSummary())
                    .setLocation(eventToAdd.getLocation())
                    .setDescription(eventToAdd.getDescription())
                    .setStart(new EventDateTime().setDateTime(new DateTime(eventToAdd.getStartDate())))
                    .setEnd(new EventDateTime().setDateTime(new DateTime(eventToAdd.getEndDate())));

            // Update the event
            event = service.events().insert(eventToAdd.getUserId(), event).execute();
    }

}

