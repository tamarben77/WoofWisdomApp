package com.example.woofwisdomapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addVaccination extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccination);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText vaccinationNameEditText = (EditText) findViewById(R.id.vaccinationNameEditText);
        EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
        EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        EditText locationEditText = (EditText) findViewById(R.id.locationEditText);
        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered details from the EditText views
                String username = usernameEditText.getText().toString();
                String vaccinationName = vaccinationNameEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String location = locationEditText.getText().toString();

                // Create a JSON object with the details
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("vaccination_name", vaccinationName);
                    jsonObject.put("date", date);
                    jsonObject.put("description", description);
                    jsonObject.put("location", location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send a POST request to the specified URL with the JSON object as the body
                String url = "http://192.168.1.14:8091/addVaccination";
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Handle any errors that occurred during the request
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                                int statusCode = response.code();
                                // Show an alert dialog with the status code
                                AlertDialog.Builder builder = new AlertDialog.Builder(addVaccination.this);
                                builder.setTitle("Status Code");
                                builder.setMessage("The server returned status code: " + statusCode);
                                builder.setPositiveButton("OK", null);
                                builder.show();
                        } else {
                            // Handle any errors that occurred during the request
                        }
                    }
                });
            }
        });
    }
}

                /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        response -> {
                            try {
                                // Get the status code from the response
                                int statusCode = response.getInt("statusCode");
                                // Show an alert dialog with the status code
                                AlertDialog.Builder builder = new AlertDialog.Builder(addVaccination.this);
                                builder.setTitle("Status Code");
                                builder.setMessage("The server returned status code: " + statusCode);
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Handle any errors that occurred during the request
                        }
                );
// Add the request to the Volley request queue
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
            }
        });*/

