package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.woofwisdomapplication.CacheManager.NetworkUtils;
import com.example.woofwisdomapplication.DTO.UserObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {
    public static String BASE_URL;
    private SharedPreferences sharedPreferences;
    private TextView welcomeTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("IP", "192.168.1.17");
        BASE_URL ="http://" + System.getProperty("IP") + ":8091/";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillCache();

        welcomeTextView = findViewById(R.id.welcome_text);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);

        //UserUtils.displayWelcomeMessage(this, welcomeTextView);
        Button logout = findViewById(R.id.logout_button);

        TextView buttonNearestVet = (TextView) findViewById(R.id.findVetButton);
        TextView buttonsuspiciousFoodBtn = (TextView) findViewById(R.id.suspiciousFoodButton);
        TextView buttonforumsBtn = (TextView) findViewById(R.id.forumsButton);
        buttonNearestVet.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), ViewListOfVets.class
            );
            startActivity(secondActivityIntent);
        });

        buttonsuspiciousFoodBtn.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), FoodActivity.class
            );
            startActivity(secondActivityIntent);
        });

        buttonforumsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FormActivity.class));
            }
        });

        TextView vaccinations = (TextView) findViewById(R.id.vaccinationsButton);
        vaccinations.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), com.example.woofwisdomapplication.views.VaccinationsFeature.vaccinations.class
            );
            startActivity(secondActivityIntent);
        });

        TextView dogBreedsInfo = (TextView) findViewById(R.id.dogInfoButton);
        dogBreedsInfo.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), dogBreedsInfoActivity.class
            );
            startActivity(secondActivityIntent);
        });

        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), login.class
            );
            startActivity(secondActivityIntent);
        });

        logout.setOnClickListener(view -> {
            // Make an asynchronous request to logout URL
            //new LogoutAsyncTask().execute();

            // Clear sessionID in SharedPreferences
            clearSessionID();
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            welcomeTextView.setText("© 2023 Woof Wisdom. All rights reserved.");
            welcomeTextView.setTextSize(12);
            welcomeTextView.setTextColor(Color.parseColor("#888888"));
        });

        if (userJson != null) {
            // Deserialize the JSON string back into a UserObject
            UserObject user = new Gson().fromJson(userJson, UserObject.class);

            // Extract and set the first name and last name
            welcomeTextView.setText("Good " + getTimeOfDay() + ", " + user.getFirstName());
            // Set the text to bold
            welcomeTextView.setTypeface(null, Typeface.BOLD);

            // Set the text size to 16
            welcomeTextView.setTextSize(24);
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            welcomeTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    private void fillCache(){
        fetchDataFromServer(BASE_URL + "showDogFoodCategories", "food_categories");
        fetchDataFromServer(BASE_URL + "showVaccinations", "all_vaccinations");
        //fetchDataFromServer(BASE_URL + "showDogFoodItemsByCategory", "food_items");
        fetchDataFromServer(BASE_URL + "dogForums/showAllForumsPost", "all_forums");
        fetchDataFromServer(BASE_URL + "dogBreed/breedsList", "all_breeds");
    }

    private void fetchDataFromServer(String serverUrl, final String cacheKey) {
        NetworkUtils.fetchDataAsync(this, serverUrl, new NetworkUtils.DataCallback() {
            @Override
            public void onDataFetched(String data) {
                // Data has been fetched and stored in the cache with the given key
            }
        });
    }

    public static String getTimeOfDay() {
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(12, 0))) {
            return "Morning";
        } else if (currentTime.isAfter(LocalTime.of(12, 0)) && currentTime.isBefore(LocalTime.of(16, 0))) {
            return "Noon";
        } else if (currentTime.isAfter(LocalTime.of(16, 0)) && currentTime.isBefore(LocalTime.of(19, 0))) {
            return "Afternoon";
        } else if (currentTime.isAfter(LocalTime.of(19, 0)) && currentTime.isBefore(LocalTime.of(23, 0))) {
            return "Evening";
        } else {
            return "Night";
        }
    }

    private class LogoutAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Create a URL object
                URL url = new URL(BASE_URL + "auth/logout");

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set up the connection
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                // Close the connection
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void clearSessionID() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sessionID", "");
        editor.apply();
    }
}