package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static String BASE_URL;
    private SharedPreferences sharedPreferences;
    private TextView welcomeTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("IP", "192.168.1.15");
        BASE_URL ="http://" + System.getProperty("IP") + ":8091/";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcome_text);
        UserUtils.displayWelcomeMessage(this, welcomeTextView);

        TextView buttonNearestVet = (TextView) findViewById(R.id.findVetButton);
        TextView buttonsuspiciousFoodBtn = (TextView) findViewById(R.id.suspiciousFoodButton);
        TextView buttonforumsBtn = (TextView) findViewById(R.id.forumsButton);
        buttonNearestVet.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), findNearestVetActivity.class
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
    }
}