package com.example.woofwisdomapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        Spinner vaccinationNameSpinner = findViewById(R.id.vaccinationNameSpinner);
        String[] vaccinationNames = {"Distemper", "Parvovirus", "Bordetella", "DHPP", "Influenza", "Leptospirosis", "Lyme Disease", "Rabies", "Coronavirus"}; // Replace this with your list of vaccination names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vaccinationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccinationNameSpinner.setAdapter(adapter);

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        EditText locationEditText = (EditText) findViewById(R.id.locationEditText);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered details from the EditText views
                String username = usernameEditText.getText().toString();
                //String vaccinationName = vaccinationNameEditText.getText().toString();
                String vaccinationName = vaccinationNameSpinner.getSelectedItem().toString();

                Button datePickerButton = findViewById(R.id.datePickerButton);
                String dateStr = datePickerButton.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = dateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTime(date);
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault());
                String selectedDateTimeStr = dateTimeFormat.format(selectedCalendar.getTime());
                String description = descriptionEditText.getText().toString();
                String location = locationEditText.getText().toString();

                // Create a JSON object with the details
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("vaccination_name", vaccinationName);
                    jsonObject.put("date", selectedDateTimeStr);
                    jsonObject.put("description", description);
                    jsonObject.put("location", location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send a POST request to the specified URL with the JSON object as the body
                String url = "http://192.168.1.11:8091/addVaccination";
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build();

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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    builder.show();
                                }
                            });                        } else {
                            // Handle any errors that occurred during the request
                        }
                    }
                });
            }
        });
    }

    public void showDatePickerDialog(View v) {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Button datePickerButton = findViewById(R.id.datePickerButton);
            datePickerButton.setText(format.format(selectedDate.getTime()));
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}


