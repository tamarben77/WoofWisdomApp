package com.example.woofwisdomapplication;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.CacheManager.CacheManager;
import com.example.woofwisdomapplication.DTO.Vet;
import com.example.woofwisdomapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.common.reflect.TypeToken;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewListOfVets extends AppCompatActivity {
    private CacheManager cacheManager;
    private FusedLocationProviderClient fusedLocationClient;
    private Double latitude, longitude;
    private int radius;
    private static String URL = "http://" + System.getProperty("IP") + ":8091/getNearestVet";
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_vets);

        cacheManager = new CacheManager(this);

        if (getIntent().hasExtra("radius")) {
            radius = getIntent().getIntExtra("radius", 0);
        }

        loader=(ProgressBar)findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create location request object
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds

        // Check if user has granted location permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location location = locationResult.getLastLocation();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        // Do something with latitude and longitude
                        String requestBody = "{\"client_latitude\": \"" + latitude + "\", \"client_longitude\": \"" + longitude + "\"}";
                        JSONObject jsonRequestBody = null;
                        try {
                            jsonRequestBody = new JSONObject(requestBody);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));

                        Type dataType = new TypeToken<List<Vet>>(){}.getType();
                        List<Vet> cachedData = cacheManager.getData("vet_list", dataType);
                        if (cachedData.size() != 0) {
                            displayVets(cachedData);
                        } else {
                            // Fetch data from the server and save it in the cache
                            new NetworkCallAsyncTask(headers, body).execute(URL);
                            //fetchDataFromServer();
                        }

                        // Run the network call in an AsyncTask
                    }
                }
            }, Looper.getMainLooper());
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private class NetworkCallAsyncTask extends AsyncTask<String, Void, String> {
        private Map<String, String> headers;
        private RequestBody requestBody;

        public NetworkCallAsyncTask(Map<String, String> headers, RequestBody requestBody) {
            this.headers = headers;
            this.requestBody = requestBody;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(urls[0]).newBuilder();
            urlBuilder.addQueryParameter("radius", String.valueOf(radius*1000));
            String urlWithParams = urlBuilder.build().toString();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlWithParams)
                    .headers(Headers.of(headers))
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray resultsArray = responseJson.getJSONArray("results");

                    List<Vet> vetList = new ArrayList<>();

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject vetJson = resultsArray.getJSONObject(i);
                        String name = vetJson.getString("name");
                        String address = vetJson.getString("vicinity");
                        double rating = vetJson.optDouble("rating", 0.0);

                        Vet vet = new Vet(name, address, rating);
                        vetList.add(vet);
                    }

                    displayVets(vetList); // Call a method to display the vets
                    // Save the fetched data in the cache
                    Type dataType = new TypeToken<List<Vet>>(){}.getType();
                    cacheManager.saveData("vet_list", vetList, dataType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                loader.setVisibility(View.GONE);
            }
        }
    }

    private void displayVets(List<Vet> vetList) {
        // Find the TextView or RecyclerView where you want to display the vets
        RecyclerView vetRecyclerView = findViewById(R.id.vetRecyclerView);

        // Create an adapter for the RecyclerView
        VetAdapter vetAdapter = new VetAdapter(vetList);
        vetRecyclerView.setAdapter(vetAdapter);

        // Set the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vetRecyclerView.setLayoutManager(layoutManager);

        // Hide the progress loader
        loader.setVisibility(View.GONE);
    }

}

