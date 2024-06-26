package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class oldMainActivity extends AppCompatActivity {
    public static String BASE_URL;
    private SharedPreferences sharedPreferences;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("IP", "192.168.1.15");
        BASE_URL ="http://" + System.getProperty("IP") + ":8091/";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        UserUtils.displayWelcomeMessage(this, welcomeTextView);

        ImageButton buttonNearestVet = (ImageButton) findViewById(R.id.mapsBtn);
        ImageButton buttonsuspiciousFoodBtn = (ImageButton) findViewById(R.id.suspiciousFoodBtn);
        ImageButton buttonforumsBtn = (ImageButton) findViewById(R.id.forumsBtn);
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

        ImageButton vaccinations = (ImageButton) findViewById(R.id.vaccinationsBtn);
        vaccinations.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), com.example.woofwisdomapplication.views.VaccinationsFeature.vaccinations.class
            );
            startActivity(secondActivityIntent);
        });

/*        ImageButton vaccinations = (ImageButton) findViewById(R.id.vaccinationsBtn);
        vaccinations.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), com.example.woofwisdomapplication.views.VaccinationsFeature.ImmunizationsRecordActivity.class
            );
            startActivity(secondActivityIntent);
        });*/

        ImageButton stoolPukeAnalyzer = (ImageButton) findViewById(R.id.cameraAnalyzer);
        stoolPukeAnalyzer.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), stoolPukeAnalyzerActivity.class
            );
            startActivity(secondActivityIntent);
        });

//        ImageButton suspiciousFood = (ImageButton) findViewById(R.id.suspiciousFoodBtn);
//        suspiciousFood.setOnClickListener(view -> {
//            Intent secondActivityIntent = new Intent(
//                    getApplicationContext(), suspiciousFoodActivity.class
//            );
//            startActivity(secondActivityIntent);
//        });

//        ImageButton forums = (ImageButton) findViewById(R.id.forumsBtn);
//        forums.setOnClickListener(view -> {
//            Intent secondActivityIntent = new Intent(
//                    getApplicationContext(), forumsActivity.class
//            );
//            startActivity(secondActivityIntent);
//        });

        ImageButton dogBreedsInfo = (ImageButton) findViewById(R.id.dogBreedsInfoBtn);
        dogBreedsInfo.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), dogBreedsInfoActivity.class
            );
            startActivity(secondActivityIntent);
        });

        Button login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), login.class
            );
            startActivity(secondActivityIntent);
        });
    }
}