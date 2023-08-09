package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    Button login_button,signup_button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login_button=(Button) findViewById(R.id.login_button);
        signup_button=(Button) findViewById(R.id.signup_button);

       /* Login Button :- */
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent it=new Intent(getApplicationContext(), LoginActivity.class);
               startActivity(it);
            }
        });

        /* SignUp Button :- */
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

    }
}