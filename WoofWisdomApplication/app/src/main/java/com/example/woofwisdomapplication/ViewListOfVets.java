package com.example.woofwisdomapplication;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewListOfVets extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private Double latitude, longitude;
    private static String URL = "http://192.168.1.11:8091/getNearestVet";
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_vets);

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

                        // Run the network call in an AsyncTask
                        new NetworkCallAsyncTask(headers, body).execute(URL);
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
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urls[0])
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
                TextView listOfVets = findViewById(R.id.listOfVets);
                listOfVets.setText(result);
                loader.setVisibility(View.GONE);
            } else {
                TextView listOfVets = findViewById(R.id.listOfVets);
                listOfVets.setText("failure");
                loader.setVisibility(View.GONE);
            }
        }
    }
}

