package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("IP", "192.168.1.37");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonNearestVet = (ImageButton) findViewById(R.id.mapsBtn);
        buttonNearestVet.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), findNearestVetActivity.class
            );
            startActivity(secondActivityIntent);
        });

        ImageButton vaccinations = (ImageButton) findViewById(R.id.vaccinationsBtn);
        vaccinations.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), com.example.woofwisdomapplication.views.VaccinationsFeature.vaccinations.class
            );
            startActivity(secondActivityIntent);
        });

        ImageButton stoolPukeAnalyzer = (ImageButton) findViewById(R.id.cameraAnalyzer);
        stoolPukeAnalyzer.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), stoolPukeAnalyzerActivity.class
            );
            startActivity(secondActivityIntent);
        });

        ImageButton suspiciousFood = (ImageButton) findViewById(R.id.suspiciousFoodBtn);
        suspiciousFood.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), suspiciousFoodActivity.class
            );
            startActivity(secondActivityIntent);
        });

        ImageButton forums = (ImageButton) findViewById(R.id.forumsBtn);
        forums.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), forumsActivity.class
            );
            startActivity(secondActivityIntent);
        });

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