package com.example.woofwisdomapplication.views.VaccinationsFeature;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.woofwisdomapplication.MainActivity;
import com.example.woofwisdomapplication.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.Calendar;
import android.content.IntentSender;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/*
import java.util.Calendar;
*/
import java.util.Locale;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class ImmunizationsRecordActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private java.util.Calendar selectedDateTime;

    private static final String TAG = ImmunizationsRecordActivity.class.getSimpleName();
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_PERMISSIONS = 1001;
    private static final int REQUEST_AUTHORIZATION = 1002;
    private GoogleAccountCredential mCredential;
    private GoogleApiClient mGoogleApiClient;
    private EditText etSummary, etLocation, etDate;
    private Button btnSelectDate;
    String vaccinationName;
    private Integer inWeeks, inGeneral;

    private RelativeLayout relativeLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);

        relativeLayout = findViewById(R.id.relativeLayout);

        Intent intent = getIntent();
        if (intent != null) {
            vaccinationName = intent.getStringExtra("vaccinationName");
            inWeeks = intent.getIntExtra("inWeeks", 0); // Default value is 0
            inGeneral = intent.getIntExtra("inGeneral", 0); // Default value is 0
        }

            // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("593298902212-bcv0nnl1pn56e3edjcs4g0r2raf4dmsn.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to Google Sign-In API and the options specified
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize Google Calendar API credentials
        mCredential = GoogleAccountCredential.usingOAuth2(
                        this, Arrays.asList(CalendarScopes.CALENDAR))
                .setBackOff(new ExponentialBackOff());

        etSummary = findViewById(R.id.etSummary);

        etSummary.setText("WoofWisdon Reminders: " + vaccinationName + " Vaccination");

        etLocation = findViewById(R.id.etLocation);
        etLocation.setText("My Favorite Vet!");

        selectedDateTime = java.util.Calendar.getInstance();

        btnSelectDate = findViewById(R.id.btnSelectDate);
        final java.util.Calendar currentDateTime = selectedDateTime;
        if (inWeeks > 0) {
            currentDateTime.add(java.util.Calendar.WEEK_OF_YEAR, inWeeks);
        } else {
            currentDateTime.add(java.util.Calendar.YEAR, inGeneral);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Update the text of the date selection button
        String formattedDateTime = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td %1$tH:%1$tM", currentDateTime);
        btnSelectDate.setText(formattedDateTime);

        btnSelectDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        Button btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEvent();
            }
        });
    }

    private void signIn() {
        String[] accountTypes = new String[]{"com.google"};
        Intent accountPickerIntent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(accountPickerIntent, REQUEST_ACCOUNT_PICKER);
    }

    private void insertEvent() {
        String summary = etSummary.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String formattedDateTime = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td %1$tH:%1$tM", selectedDateTime);


        if (summary.isEmpty() || location.isEmpty() || formattedDateTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        } else {
            new InsertEventTask(mCredential, summary, location, formattedDateTime).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                // User provided consent, re-attempt to insert the event
                insertEvent();
            } else {
                Toast.makeText(this, "Authorization denied.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_ACCOUNT_PICKER && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            }
        }
    }


    private void chooseAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent accountPickerIntent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(accountPickerIntent, REQUEST_ACCOUNT_PICKER);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseAccount();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check Google Play Services availability
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, REQUEST_AUTHORIZATION).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private class InsertEventTask extends AsyncTask<Void, Void, Void> {

        private GoogleAccountCredential credential;
        private String summary, location, dateString;

        InsertEventTask(GoogleAccountCredential credential, String summary, String location, String dateString) {
            this.credential = credential;
            this.summary = summary;
            this.location = location;
            this.dateString = dateString;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Calendar service = new Calendar.Builder(
                        Utils.HTTP_TRANSPORT,
                        Utils.JSON_FACTORY,
                        credential)
                        .setApplicationName("Woof Wisdom App")
                        .build();

                Event event = new Event()
                        .setSummary(summary)
                        .setLocation(location);

                DateTime startDateTime = Utils.parseDateTime(dateString);
                DateTime endDateTime = Utils.parseDateTime(dateString);

                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("UTC");

                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("UTC");

                event.setStart(start);
                event.setEnd(end);

                service.events().insert("primary", event).execute();

            } catch (UserRecoverableAuthIOException e) {
                // User needs to provide consent, start an activity to prompt for permission
                Intent intent = e.getIntent();
                startActivityForResult(intent, REQUEST_AUTHORIZATION);
                Log.e(TAG, "UserRecoverableAuthIOException: NeedRemoteConsent");
                e.printStackTrace();
            }catch (IOException | ParseException e) {
                Log.e(TAG, "Error inserting event: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ImmunizationsRecordActivity.this, "Event inserted successfully", Toast.LENGTH_SHORT).show();
            etSummary.setText("");
            etLocation.setText("");
            btnSelectDate.setText("select date");

            // Close the activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ImmunizationsRecordActivity.this.finish();
                }
            }, 2000); // Adjust the delay as needed (in milliseconds)
        }
    }

    private void showDateTimePicker() {
        final java.util.Calendar currentDateTime = selectedDateTime;
        if (inWeeks > 0) {
            currentDateTime.add(java.util.Calendar.WEEK_OF_YEAR, inWeeks);
        } else {
            currentDateTime.add(java.util.Calendar.YEAR, inGeneral);
        }

        // Update the text of the date selection button
        String formattedDateTime = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td %1$tH:%1$tM", currentDateTime);
        btnSelectDate.setText(formattedDateTime);
        int year = currentDateTime.get(java.util.Calendar.YEAR);
        int month = currentDateTime.get(java.util.Calendar.MONTH);
        int day = currentDateTime.get(java.util.Calendar.DAY_OF_MONTH);
        int hour = currentDateTime.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = currentDateTime.get(java.util.Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentDateTime.set(java.util.Calendar.YEAR, year);
                currentDateTime.set(java.util.Calendar.MONTH, month);
                currentDateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ImmunizationsRecordActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentDateTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                        currentDateTime.set(java.util.Calendar.MINUTE, minute);

                        // Update the text of the date selection button
                        String formattedDateTime = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td %1$tH:%1$tM", currentDateTime);
                        btnSelectDate.setText(formattedDateTime);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            // Handle "Home" click here, maybe go to the main activity or dashboard
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_return) {
            // Handle "Return" click, maybe just close the current activity
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
