package com.example.woofwisdomapplication.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.woofwisdomapplication.R;

public class MainActivity2 extends AppCompatActivity {
    /*FoodActivity food_fragment=new FoodActivity();
    FormActivity form_fragment=new FormActivity();
    RelativeLayout fragment_replace;
    BottomNavigationView bottom_navigation_menu;*/

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        /*fragment_replace=(RelativeLayout) findViewById(R.id.replace);
        bottom_navigation_menu=(BottomNavigationView) findViewById(R.id.bottom_navigation_menu);

        *//* Fragment Replacement :- *//*
        getSupportFragmentManager().beginTransaction().replace(R.id.replace,form_fragment).commit();
        bottom_navigation_menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.form:
                        fragment = new FormActivity();
                        loadFragment(fragment);
                        return true;
                    case R.id.food:
                        fragment = new FoodActivity();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }

            private void loadFragment(Fragment fragment) {
                // load fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace, fragment);
                transaction.commit();
            }
        });*/

    }
}