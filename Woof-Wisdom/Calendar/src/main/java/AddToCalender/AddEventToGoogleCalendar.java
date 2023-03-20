package AddToCalender;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class AddEventToGoogleCalendar {
        private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        private static final String APPLICATION_NAME = "My Application Name";
        private static final String CREDENTIALS_FILE_PATH = "src/main/resources/keyfile.json";
        private static final String CLIENT_SECRET_FILE_PATH = "src/main/resources/client_secret.json";
        private static final String[] SCOPES = {CalendarScopes.CALENDAR};
        private static final String TIME_ZONE = "GMT-05:00"; // change to the appropriate timezone
        //private static final String CALENDAR_ID = "primary"; // change to the appropriate calendar ID
        private static final String CALENDAR_ID = "neta.vega@gmail.com"; // change to the appropriate calendar ID

        private static Calendar getCalendarService() throws IOException, GeneralSecurityException {
                HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                Credential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                        .createScoped(Collections.singletonList(CalendarScopes.CALENDAR));
                return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        }

        public static void addEventToGoogleCalendar(String summary, String description, String location, String startDate, String endDate, String calendarId) throws GeneralSecurityException, IOException, ParseException {
                Event event = new Event();
                event.setSummary(summary);
                event.setDescription(description);
                event.setLocation(location);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
                Date StartDate = dateFormat.parse(startDate);
                event.setStart(new EventDateTime().setDateTime(new DateTime(StartDate)).setTimeZone(TIME_ZONE));
                Date EndDate = dateFormat.parse(endDate);
                event.setEnd(new EventDateTime().setDateTime(new DateTime(EndDate)).setTimeZone(TIME_ZONE));
                //event.setStart(new EventDateTime().setDateTime(new Date(startDate).getTime()).setTimeZone(TIME_ZONE));
                //event.setEnd(new EventDateTime().setDateTime(new Date(req.getParameter("end")).getTime()).setTimeZone(TIME_ZONE));

                Calendar service = getCalendarService();
                service.events().insert(calendarId, event).execute();
        }
}
