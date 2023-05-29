package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.woofwisdomapplication.DTO.GoogleCalendarEvent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.example.woofwisdomapplication.DTO.Vaccination;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class immunizationsRecordActivity extends AppCompatActivity {


    /*private static final String URL = "http://192.168.1.11:8091/addToCalender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);
        GoogleCalendarEvent event = new GoogleCalendarEvent();
        event = createGoogleEvent();
        String requestBody = new Gson().toJson(event);
        JSONObject jsonRequestBody = null;
        try {
            jsonRequestBody = new JSONObject(requestBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
        new SendPostRequestTask(headers, body).execute(URL);
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

    private class SendPostRequestTask extends AsyncTask<String, Void, String> {

        private Map<String, String> headers;
        private RequestBody requestBody;

        public SendPostRequestTask(Map<String, String> headers, RequestBody requestBody) {
            this.headers = headers;
            this.requestBody = requestBody;
        }
        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urls[0])
                    .headers(Headers.of(headers))
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Do something with the response, e.g. print it
            System.out.println(result);
        }
    }

    private String sendPostRequest(GoogleCalendarEvent googleCalendarEvent) throws IOException {
        URL url = new URL("http://192.168.1.11:8091/addToCalender");
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

        return response;
    }*/
}
