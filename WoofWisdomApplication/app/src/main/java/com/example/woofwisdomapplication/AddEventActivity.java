package com.example.woofwisdomapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddEventActivity extends AppCompatActivity {

    private int year, month, day, hour, minute;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    HttpURLConnection urlConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Start Date Button Click Listener
        Button startDateButton = findViewById(R.id.startDateButton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show DatePicker
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update Start Date EditText
                                EditText startDateEditText = findViewById(R.id.startDateEditText);
                                startDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

// Start Time Button Click Listener
        Button startTimeButton = findViewById(R.id.startTimeButton);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show TimePicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update Start Date EditText
                                EditText startDateEditText = findViewById(R.id.startDateEditText);
                                String startTimeFormat = String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minute);
                                String currentDate = startDateEditText.getText().toString().split(" ")[0];
                                startTime = String.format("%s%s", currentDate, startTimeFormat);
                                startDateEditText.setText(startTime);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(AddEventActivity.this));
                timePickerDialog.show();
            }
        });

// End Date Button Click Listener
        Button endDateButton = findViewById(R.id.endDateButton);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show DatePicker
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update End Date EditText
                                EditText endDateEditText = findViewById(R.id.endDateEditText);
                                endDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                endDateEditText.setText(endDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

// End Time Button Click Listener
        Button endTimeButton = findViewById(R.id.endTimeButton);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show TimePicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update End Date EditText
                                EditText endDateEditText = findViewById(R.id.endDateEditText);
                                String endTimeFormat = String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minute);
                                String currentDate = endDateEditText.getText().toString().split(" ")[0];
                                endTime = String.format("%s%s", currentDate, endTimeFormat);
                                endDateEditText.setText(endTime);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(AddEventActivity.this));
                timePickerDialog.show();
            }
        });


            Button saveEvent = (Button) findViewById(R.id.saveEventBtn);
        saveEvent.setOnClickListener(view -> {
            JSONObject json = new JSONObject();
            try {
                EditText summaryEditText = (EditText) findViewById(R.id.summaryEditText);
                EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
                EditText locationEditText = (EditText) findViewById(R.id.locationEditText);
                EditText userEmailEditText = (EditText) findViewById(R.id.userEmailEditText);

                json.put("summary", summaryEditText.getText().toString());
                json.put("description", descriptionEditText.getText().toString());
                json.put("location", locationEditText.getText().toString());
                json.put("userEmail", userEmailEditText.getText().toString());

                StringBuilder start = new StringBuilder();
                start.append(startDate);
                start.append("T");
                start.append(startTime);
                start.append(".000Z");
                json.put("startDate", start.toString());
                StringBuilder end = new StringBuilder();
                end.append(endDate);
                end.append("T");
                end.append(endTime);
                end.append(".000Z");
                json.put("endDate", end.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AddToCalendarTask task = new AddToCalendarTask(json);
            task.execute();
        });


    }


    public class AddToCalendarTask extends AsyncTask<Void, Void, Boolean> {

        private static final String TAG = "AddToCalendarTask";
        private static final String URL_STRING = "http://192.168.1.11:8091/addToCalender";

        private final JSONObject jsonObject;

        public AddToCalendarTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(URL_STRING);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();

                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "HTTP error code: " + statusCode);
                    return false;
                }
                return true;
            } catch (IOException e) {
                Log.e(TAG, "IO error", e);
                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                int responseCode;
                try {
                    responseCode = urlConnection.getResponseCode();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                    builder.setMessage("Response code: " + responseCode)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (IOException e) {
                    Log.e(TAG, "IO error", e);
                }
            } else {
                int responseCode;
                try {
                    responseCode = urlConnection.getResponseCode();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                    builder.setMessage("Response code: " + responseCode)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (IOException e) {
                    Log.e(TAG, "IO error", e);
                }
                Log.e(TAG, "Failed to send data");
            }
        }
    }
}