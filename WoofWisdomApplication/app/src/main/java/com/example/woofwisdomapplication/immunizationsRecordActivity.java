package com.example.woofwisdomapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class immunizationsRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);
        Button addEvent = (Button) findViewById(R.id.saveEventBtn);
        addEvent.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddEventActivity.class);
            startActivity(intent);
        });

        /*Button addEvent = (Button) findViewById(R.id.addEventBtn);
        addEvent.setOnClickListener(view -> {
            JSONObject json = new JSONObject();
            try {
                json.put("summary", "Test Event3");
                json.put("description", "blabla");
                json.put("location", "home");
                json.put("startDate", "2023-03-16T14:30:00.000Z");
                json.put("endDate", "2023-03-16T14:30:00.000Z");
                json.put("userEmail", "neta.vega@gmail.com");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AddToCalendarTask task = new AddToCalendarTask(json);
            task.execute();
        });*/
    }



}


/*
package com.example.woofwisdomapplication;

import static android.content.ContentValues.TAG;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class immunizationsRecordActivity extends AppCompatActivity{

    private static final String URL = "http://192.168.1.11:8091/addToCalender";
    private static final String CLIENT_ID = "my_client_id";
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations_record);

        String summary = "My Event";
        String description = "Description of my event";
        String location = "San Francisco, CA";
        String start = "2023-03-17T10:00:00-07:00";
        String end = "2023-03-17T11:00:00-07:00";
        String userEmail = "neta.vega@gmail.com";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID)
                .requestEmail()
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        Button addEvent = (Button) findViewById(R.id.addEventBtn);
        addEvent.setOnClickListener(view -> {
            AddToCalendarTask addToCalendarTask = new AddToCalendarTask(this);
            addToCalendarTask.execute(summary, description, location, start, end, userEmail);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, get account information here if needed
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private class AddToCalendarTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "AddToCalendarTask";
        private static final String ACCOUNT_TYPE = "com.google";
        private static final String AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/calendar";
        private static final String CLIENT_ID = "your-client-id"; // replace with your client ID
        private static final String SERVER_URL = "http://192.168.1.11:8091/addToCalendar";

        private final Context context;

        public AddToCalendarTask(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                AccountManager accountManager = AccountManager.get(context);
                Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
                if (accounts.length > 0) {
                    String authToken = GoogleAuthUtil.getToken(context, accounts[0], AUTH_SCOPE);
                    if (authToken != null) {
                        GoogleCredential credential = new GoogleCredential().setAccessToken(authToken);
                        URL url = new URL(SERVER_URL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Authorization", "Bearer " + authToken);
                        connection.setDoOutput(true);

                        String summary = params[0];
                        String description = params[1];
                        String location = params[2];
                        String start = params[3];
                        String end = params[4];
                        String userEmail = params[5];

                        String json = String.format("{\"summary\":\"%s\",\"description\":\"%s\",\"location\":\"%s\"," +
                                        "\"startDate\":\"%s\",\"endDate\":\"%s\",\"userEmail\":\"%s\"}",
                                summary, description, location, start, end, userEmail);
                        connection.getOutputStream().write(json.getBytes());

                        int responseCode = connection.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {
                            Log.e(TAG, "Failed to add event to calendar: " + responseCode);
                        }
                    } else {
                        Log.e(TAG, "Failed to get auth token");
                    }
                } else {
                    Log.e(TAG, "No Google account found on device");
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return null;
        }
    }
}
*/
