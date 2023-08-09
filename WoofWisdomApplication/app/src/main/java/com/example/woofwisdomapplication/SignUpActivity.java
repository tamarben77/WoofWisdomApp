package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    /*ImageView back_button;
    Button signup_button;
    TextView signup_link;
    Animation slide_left,blink;*/

    Animation slide_left,blink;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNameEditText = findViewById(R.id.editTextTextFirstName);
        lastNameEditText = findViewById(R.id.editTextTextLastName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        Button signUpButton = findViewById(R.id.signUpButton);
//        TextView signup_link = (TextView) findViewById(R.id.link);
//        ImageView back_button = (ImageView) findViewById(R.id.back);

//
//        signup_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signup_link.startAnimation(blink);
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            }
//        });
//
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                back_button.startAnimation(slide_left);
//                finish();
//            }
//        });

        slide_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_animation);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_animation);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String url = "http://localhost:8091/signIn" + firstName + "&lastName=" + lastName + "&email=" + email + "&password=" + password;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Successfully SignUp", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to SignUp, please try again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Volley.newRequestQueue(this).add(request);
        /*back_button = (ImageView) findViewById(R.id.back);
        signup_button = (Button) findViewById(R.id.signup);
        signup_link = (TextView) findViewById(R.id.link);*/
    }
}
/* Animation :- *//*
        slide_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_animation);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_animation);

        *//* Back Button :- *//*
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_button.startAnimation(slide_left);
                finish();
            }
        });

        *//* SignUp Button :- *//*
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "Thanks For SignUp", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        *//* SignUp Link Button :- *//*
        signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_link.startAnimation(blink);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });*/
