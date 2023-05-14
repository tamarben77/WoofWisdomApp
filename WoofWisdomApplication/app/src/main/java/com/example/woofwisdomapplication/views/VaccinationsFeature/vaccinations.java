package com.example.woofwisdomapplication.views.VaccinationsFeature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.woofwisdomapplication.API.VaccinationService;
import com.example.woofwisdomapplication.DTO.Vaccination;
import com.example.woofwisdomapplication.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vaccinations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VaccinationAdapter adapter;

    private ProgressBar loader;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinations);
        loader=(ProgressBar)findViewById(R.id.progressBarLoader);
        ImageButton add = (ImageButton) findViewById(R.id.floatingActionButton);
        add.setOnClickListener(view -> {
            Intent secondActivityIntent = new Intent(
                    getApplicationContext(), addVaccination.class
            );
            startActivity(secondActivityIntent);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VaccinationAdapter();
        recyclerView.setAdapter(adapter);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11:8091/showVaccinations/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VaccinationService service = retrofit.create(VaccinationService.class);
        Call<List<Vaccination>> call = service.getVaccinations();

        call.enqueue(new Callback<List<Vaccination>>() {
            @Override
            public void onResponse(Call<List<Vaccination>> call, retrofit2.Response<List<Vaccination>> response) {
                loader.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Vaccination> vaccinations = response.body();
                    adapter.setVaccinations(vaccinations);
                } else {
                    Log.e("VaccinationsActivity", "Failed to get vaccinations");
                }
            }

            @Override
            public void onFailure(Call<List<Vaccination>> call, Throwable t) {
                Log.e("VaccinationsActivity", "Failed to get vaccinations", t);
            }
        });



    }
}