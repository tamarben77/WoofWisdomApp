package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class signUp extends AppCompatActivity {
    private static final String IP = System.getProperty("IP");
    private static final String baseUrl = "http://" +IP+ ":8091/auth/signUp";
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText dogNameEditText;
    private EditText dogWeightEditText;
    private EditText dogAgeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.editTextTextFirstName);
        lastNameEditText = findViewById(R.id.editTextTextLastName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        dogNameEditText = findViewById(R.id.editTextDogName);
        dogWeightEditText = findViewById(R.id.editTextDogWeight);
        dogAgeEditText = findViewById(R.id.editTextDogAge);

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

   /* private void signUpUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String dogName = dogNameEditText.getText().toString();
        String dogWeight = dogWeightEditText.getText().toString();
        String dogAge = dogAgeEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email, password, and first name are required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = baseUrl + "?firstName=" + firstName + "&lastName=" + lastName + "&email=" + email + "&password=" + password;

        if (!dogName.isEmpty()) {
            url += "&dogName=" + dogName;
        }
        if (!dogWeight.isEmpty()) {
            url += "&dogWeight=" + dogWeight;
        }
        if (!dogAge.isEmpty()) {
            url += "&dogAge=" + dogAge;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Successfully SignUp", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(signUp.this, MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to SignUp, please try again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(signUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Volley.newRequestQueue(this).add(request);
    }*/
   private void signUpUser() {
       String firstName = firstNameEditText.getText().toString();
       String lastName = lastNameEditText.getText().toString();
       String email = emailEditText.getText().toString();
       String password = passwordEditText.getText().toString();
       String dogName = dogNameEditText.getText().toString();
       String dogWeight = dogWeightEditText.getText().toString();
       String dogAge = dogAgeEditText.getText().toString();

       if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
           Toast.makeText(getApplicationContext(), "Email, password, and first name are required fields.", Toast.LENGTH_LONG).show();
           return;
       }

       String url = baseUrl + "?firstName=" + firstName + "&lastName=" + lastName + "&email=" + email + "&password=" + password;

       if (!dogName.isEmpty()) {
           url += "&dogName=" + dogName;
       }
       if (!dogWeight.isEmpty()) {
           url += "&dogWeight=" + dogWeight;
       }
       if (!dogAge.isEmpty()) {
           url += "&dogAge=" + dogAge;
       }

       JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               Toast.makeText(getApplicationContext(), "Successfully SignUp", Toast.LENGTH_SHORT).show();
               // Store the session ID in SharedPreferences
               SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               try {
                   editor.putString("sessionID", response.getString("sessionID"));
                   editor.apply();
               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }
               // Redirect to MainActivity and display welcome message
               Intent intent = new Intent(signUp.this, oldMainActivity.class);
               startActivity(intent);
               finish();
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(), "Failed to SignUp, please try again", Toast.LENGTH_SHORT).show();
           }
       });
       int timeout = 60000;
       request.setRetryPolicy(new DefaultRetryPolicy(timeout,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       Volley.newRequestQueue(this).add(request);
   }
}
