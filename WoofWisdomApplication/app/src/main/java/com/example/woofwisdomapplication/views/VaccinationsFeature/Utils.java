package com.example.woofwisdomapplication.views.VaccinationsFeature;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String APPLICATION_NAME = "Woof Wisdom App";
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final NetHttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing HTTP transport", e);
        }
    }

    public static Date parseDateTime(String dateTimeString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.parse(dateTimeString);
    }
}

