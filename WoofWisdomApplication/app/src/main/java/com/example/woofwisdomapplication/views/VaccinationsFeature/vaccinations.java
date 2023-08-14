package com.example.woofwisdomapplication.views.VaccinationsFeature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.woofwisdomapplication.API.VaccinationService;
import com.example.woofwisdomapplication.CacheManager.CacheManager;
import com.example.woofwisdomapplication.DTO.Vaccination;
import com.example.woofwisdomapplication.R;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vaccinations extends AppCompatActivity {

    private CacheManager cacheManager;
    private RecyclerView recyclerView;
    private VaccinationAdapter adapter;

    private FrameLayout progressBarLayout;
    private ProgressBar progressBarLoader;
    private TextView progressDialogText;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinations);

        progressBarLoader = findViewById(R.id.progressBarLoader);
        progressDialogText = findViewById(R.id.progressText);

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

        cacheManager = new CacheManager(this);

        Type dataType = new TypeToken<List<Vaccination>>(){}.getType();
        List<Vaccination> cachedData = cacheManager.getData("all_vaccinations", dataType);
        if (cachedData != null) {
            adapter.setVaccinations(cachedData);
        } else {
            // Fetch data from the server and save it in the cache
            fetchDataFromServer();
        }
    }

    private void fetchDataFromServer() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + System.getProperty("IP") + ":8091/showVaccinations/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        progressBarLoader.setVisibility(View.VISIBLE);

        VaccinationService service = retrofit.create(VaccinationService.class);
        Call<List<Vaccination>> call = service.getVaccinations();

        call.enqueue(new Callback<List<Vaccination>>() {
            @Override
            public void onResponse(Call<List<Vaccination>> call, retrofit2.Response<List<Vaccination>> response) {
                if (response.isSuccessful()) {
                    Type dataType = new TypeToken<List<Vaccination>>(){}.getType();
                    cacheManager.saveData("all_vaccinations", response.body(), dataType);
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