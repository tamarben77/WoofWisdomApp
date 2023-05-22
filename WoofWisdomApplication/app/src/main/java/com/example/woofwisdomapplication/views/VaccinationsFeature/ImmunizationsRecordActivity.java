package com.example.woofwisdomapplication.views.VaccinationsFeature;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.woofwisdomapplication.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

public class ImmunizationsRecordActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ImmunizationsRecordActivity.class.getSimpleName();
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_PERMISSIONS = 1001;
    private static final int REQUEST_AUTHORIZATION = 1002;

    private GoogleAccountCredential mCredential;
    private GoogleApiClient mGoogleApiClient;
    private EditText etSummary, etLocation, etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);

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
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_ACCOUNT_PICKER);
    }

    private void insertEvent() {
        String summary = etSummary.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateString = etDate.getText().toString().trim();

        if (summary.isEmpty() || location.isEmpty() || dateString.isEmpty()) {
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
            new InsertEventTask(mCredential, summary, location, dateString).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ACCOUNT_PICKER && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
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
        // Check network connection availability
        return ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
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
                com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, credential)
                        .setApplicationName("Woof Wisdom App")
                        .build();

                Event event = new Event()
                        .setSummary(summary)
                        .setLocation(location);

                DateTime startDateTime = new DateTime(Utils.parseDateTime(dateString).getTime());
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("Your Time Zone");
                event.setStart(start);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Utils.parseDateTime(dateString));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                DateTime endDateTime = new DateTime(calendar.getTime());
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("Your Time Zone");
                event.setEnd(end);

                String calendarId = "primary";
                event = service.events().insert(calendarId, event).execute();
                Log.d(TAG, "Event created: " + event.getHtmlLink());

                return null;
            } catch (IOException e) {
                Log.e(TAG, "Error inserting event: " + e.getMessage());
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(ImmunizationsRecordActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
