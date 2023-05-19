package com.example.woofwisdomapplication.views.VaccinationsFeature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.woofwisdomapplication.DTO.NextVaccination;
import com.example.woofwisdomapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NextVaccinationsActivity extends AppCompatActivity {
    private static final String IP = System.getProperty("IP");//"192.168.10.57";
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_vaccinations);

        linearLayout = findViewById(R.id.linearLayout);

        int dogAgeInWeeks = 12;
        // Create a Retrofit instance and interface for making the API call
        String url = "http://" + IP + ":8091/" +
                "next-vaccinations?dogAgeInWeeks=" +
                String.valueOf(dogAgeInWeeks);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NextVaccination>>() {}.getType();
                    List<NextVaccination> vaccinations = gson.fromJson(response.body().string(), listType);
                    for(NextVaccination vac: vaccinations){
                        View vaccinationItemView = LayoutInflater.from(NextVaccinationsActivity.this).inflate(R.layout.recommended_vaccination_item, null);
                        TextView txtVaccinationName = vaccinationItemView.findViewById(R.id.txtVaccinationName);
                        TextView txtVaccinationDetails = vaccinationItemView.findViewById(R.id.txtVaccinationDetails);
                        Button btnSetReminder = vaccinationItemView.findViewById(R.id.btnSetReminder);

                        txtVaccinationName.setText(vac.getName());

                        String details = "";
                        if (vac.getInWeeks() > 0) {
                            details += "In " + vac.getInWeeks() + " weeks";
                        }
                        if (vac.getInGeneral() != 0) {
                            if (!details.isEmpty()) {
                                details += ", ";
                            }
                            details += "In general " + vac.getInGeneral();
                        }
                        txtVaccinationDetails.setText(details);

                        btnSetReminder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Perform reminder setup logic here
                            }
                        });

                        linearLayout.addView(vaccinationItemView);
                    }
                } else {
                    // Handle the error
                    //Toast.makeText(AddVaccinationActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}