package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
TextView app_name;
Animation slide_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app_name=(TextView) findViewById(R.id.app_name);

       /* Animation :- */
        slide_up= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up_animation);
    }
    @Override
    public void onResume(){
        app_name.startAnimation(slide_up);
        super.onResume();
        Thread td= new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashActivity.this, oldMainActivity.class));
                }
            }
        });td.start();
    }
}