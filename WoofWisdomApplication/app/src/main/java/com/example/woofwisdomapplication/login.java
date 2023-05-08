package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private static final String URL = "http://192.168.1.212:8091/signIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.buttontoLogin);
        Button signUpButton = findViewById(R.id.signUpbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("email", email);
                requestBody.put("password", password);
                String jsonBody = new Gson().toJson(requestBody);
                JSONObject jsonRequestBody = null;
                try {
                    jsonRequestBody = new JSONObject(jsonBody);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonRequestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    editor.putString("sessionID", response.getString("sessionID"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                editor.apply();
                                Log.d("LoginActivity", "Starting MainActivity");
                                Intent intent = new Intent(login.this, MainActivity.class);
                                Log.d("login", "Starting Main Activity");
                                startActivity(intent);
                                Log.d("login", "Main Activity started");
                                Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("login", "login Failed");
                                Toast.makeText(login.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                int timeout = 60000;
                request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(login.this).add(request);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signUp.class);
                startActivity(intent);
            }
        });
    }
}