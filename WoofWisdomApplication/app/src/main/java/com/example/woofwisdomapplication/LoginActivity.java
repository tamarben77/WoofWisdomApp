package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.oldMainActivity.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    private static final String URL = BASE_URL+"signIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView back_button;
        Animation slide_left,move,blink;
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
       // back_button=(ImageView) findViewById(R.id.back);

        TextView signUpButton = findViewById(R.id.signUpButton);

        slide_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_animation);
        move= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_animation);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_animation);

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
                                    editor.putInt("userID", Integer.parseInt(response.getString("userID")));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                editor.apply();
                                Log.d("LoginActivity", "Starting MainActivity");
                                Intent intent = new Intent(getApplicationContext(), oldMainActivity.class);
                                Log.d("login", "Starting Main Activity");
                                startActivity(intent);
                                Log.d("login", "Main Activity started");
                                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("login", "login Failed");
                                Toast.makeText(getApplicationContext(), "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), oldMainActivity.class));
                            }
                        });
                int timeout = 60000;
                request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(request);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                back_button.startAnimation(slide_left);
//                finish();
//            }
//        });
    }
}

        /*back_button=(ImageView) findViewById(R.id.back);
        forget_password=(TextView) findViewById(R.id.forget);
        signup_activity=(TextView) findViewById(R.id.link);
        login_button=(Button) findViewById(R.id.login);*/
/* Animations :- *//*
        slide_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_animation);
        move= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_animation);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_animation);
       *//* EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.buttontoLogin);
        Button signUpButton = findViewById(R.id.signUpbutton);*//*
 *//* Back Button :- *//*
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_button.startAnimation(slide_left);
                finish();
            }
        });
        *//* Forget Password Button :- *//*
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget_password.startAnimation(move);
                Toast.makeText(LoginActivity.this, "Forget Password", Toast.LENGTH_SHORT).show();
            }
        });
        *//* Login Button :- *//*
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
            }
        });
        *//* Signup Link Button :- *//*
        signup_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_activity.startAnimation(blink);
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });*/