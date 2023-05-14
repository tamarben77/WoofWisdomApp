package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class signUp extends AppCompatActivity {
    private static final String baseUrl = "http://192.168.1.212:8091/signUp";
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
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextNumberPassword);
        dogNameEditText = findViewById(R.id.editTextDogName);
        dogWeightEditText = findViewById(R.id.editTextDogWight);
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
   }
}
