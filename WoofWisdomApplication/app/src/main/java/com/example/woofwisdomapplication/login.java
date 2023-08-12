package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;

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
    private static final String IP = System.getProperty("IP");
    private static final String URL = "http://" + IP + ":8091/auth/signIn";
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUIElements();
    }

    private void setupUIElements() {
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        Log.d("mubi", URL);

        loginButton.setOnClickListener(v -> loginUser(emailEditText, passwordEditText));

        ImageView eyeIcon = findViewById(R.id.eyeIcon);
        eyeIcon.setOnClickListener(v -> togglePasswordVisibility(passwordEditText));

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, signUp.class);
            startActivity(intent);
        });
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(null);
        } else {
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void loginUser(EditText emailEditText, EditText passwordEditText) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        sendLoginRequest(requestBody);
    }

    private void sendLoginRequest(Map<String, String> requestBody) {
        String jsonBody = new Gson().toJson(requestBody);
        JSONObject jsonRequestBody;
        try {
            jsonRequestBody = new JSONObject(jsonBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonRequestBody,
                this::handleLoginResponse,
                this::handleLoginError);

        int timeout = 60000;
        request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(login.this).add(request);
    }

    private void handleLoginResponse(JSONObject response) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("sessionID", response.getString("sessionID"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        editor.apply();

        TextView loginFeedback = findViewById(R.id.loginFeedback);
        loginFeedback.setVisibility(View.GONE); // Hide the feedback text

        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
    }

    private void handleLoginError(VolleyError error) {

        TextView loginFeedback = findViewById(R.id.loginFeedback);
        loginFeedback.setText("DEBUG: This is an error message!");
        loginFeedback.setVisibility(View.VISIBLE); // Display the feedback text
        loginFeedback.setText("Login failed. Please try again.");
        Log.d("DEBUG", "Visibility: " + loginFeedback.getVisibility() + ", Text: " + loginFeedback.getText());
        Toast.makeText(login.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
//        TextView loginFeedback = findViewById(R.id.loginFeedback);
//        loginFeedback.setVisibility(View.VISIBLE); // Display the feedback text
//
//        if (error.networkResponse != null) {
//            switch (error.networkResponse.statusCode) {
//                case 401: // Unauthorized
//                    loginFeedback.setText("Incorrect email or password.");
//                    break;
//                case 404: // Not Found
//                    loginFeedback.setText("Email not registered.");
//                    break;
//                default:
//                    loginFeedback.setText("Login failed: " + error.getMessage());
//                    break;
//            }
//        } else {
//            loginFeedback.setText("Login failed: " + error.getMessage());
//        }
//        Toast.makeText(login.this, "Login failed: " + error.getMessage(), Toast.LENGTH_LONG).show();

    }
}