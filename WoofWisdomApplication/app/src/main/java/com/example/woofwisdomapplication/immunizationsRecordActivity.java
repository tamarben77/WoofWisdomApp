package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.woofwisdomapplication.DTO.GoogleCalendarEvent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;

public class immunizationsRecordActivity extends AppCompatActivity {

    private static final String URL = "http://localhost:8091/addToCalender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);
        try {
            sendPostRequest(createGoogleEvent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GoogleCalendarEvent createGoogleEvent(){
        GoogleCalendarEvent event = new GoogleCalendarEvent();
        event.setUserId("neta.vega@gmail.com");
        event.setStartDate("2023-03-01T09:00:00-07:00");
        event.setEndDate("2023-03-01T17:00:00-07:00");
        event.setSummary("test summary");
        event.setLocation("test location");
        event.setDescription("test description");
        event.setStart("America/Los_Angeles");
        event.setEnd("America/Los_Angeles");
        return event;
    }

    public void sendPostRequest(GoogleCalendarEvent googleCalendarEvent) throws IOException {
        URL url = new URL("http://localhost:8091/addToCalender");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Write the POST data to the connection's output stream
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        Gson gson = new Gson();
        String postData = gson.toJson(googleCalendarEvent);
        writer.write(postData);
        writer.flush();

        // Read the response from the server
        Scanner scanner = new Scanner(connection.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        // Do something with the response, e.g. print it
        System.out.println(response);
    }
}