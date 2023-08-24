package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.DTO.UserObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private static final String IP = System.getProperty("IP");
    private static final String URL = "http://" + IP + ":8091/auth/signIn";
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        Log.d("mubi",URL);
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

                                // Asynchronous request to get user info
                                JsonObjectRequest userRequest = null;
                                try {
                                    userRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            BASE_URL + "auth/getUserInfo?sessionID=" + response.getString("sessionID"),
                                            null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject userResponse) {
                                                    // Parse the userResponse and create a UserObject instance
                                                    UserObject user = new Gson().fromJson(userResponse.toString(), UserObject.class);

                                                    // Save the UserObject in SharedPreferences
                                                    editor.putString("user", new Gson().toJson(user));
                                                    editor.apply();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("getUserInfo", "Failed to get user info: " + error.getMessage());
                                                }
                                            }
                                    );
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                int timeout = 60000;
                                userRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Volley.newRequestQueue(login.this).add(userRequest);

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
        ImageView eyeIcon = findViewById(R.id.eyeIcon);
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    passwordEditText.setTransformationMethod(null);
                } else {
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
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