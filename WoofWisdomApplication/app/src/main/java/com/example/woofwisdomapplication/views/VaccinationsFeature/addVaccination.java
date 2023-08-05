package com.example.woofwisdomapplication.views.VaccinationsFeature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.woofwisdomapplication.DTO.NextVaccination;
import com.example.woofwisdomapplication.DTO.Vaccination;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.findNearestVetActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    private static final String IP = System.getProperty("IP");//"192.168.10.57";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccination);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing request...");
        progressDialog.setCancelable(false);

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
                progressDialog.show();

                // Retrieve the entered details from the EditText views
                String username = usernameEditText.getText().toString();
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
                String url = "http://" + IP + ":8091/addVaccination";
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
                        hideProgressIndicator();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                                int statusCode = response.code();
                                runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressIndicator();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(addVaccination.this);

                                    // Set the title and message for the dialog
                                    builder.setTitle("Get tips for future vaccinations?");
                                    builder.setMessage("Would you like to get tips for future vaccinations?");

                                    // Set the positive button (Yes)
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent secondActivityIntent = new Intent(
                                                    getApplicationContext(), NextVaccinationsActivity.class
                                            );
                                            startActivity(secondActivityIntent);
                                        }
                                    });

                                    // Set the negative button (No)
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Close the dialog
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    // Create and show the AlertDialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });

                        } else {
                            // Handle any errors that occurred during the request
                            hideProgressIndicator();
                        }
                    }
                });
            }
        });
    }
    // Method to hide the progress indicator using a delay
    private void hideProgressIndicator() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 500); // Delay for 500 milliseconds before hiding the progress indicator
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


