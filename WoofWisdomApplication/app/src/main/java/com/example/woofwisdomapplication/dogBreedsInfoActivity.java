package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class dogBreedsInfoActivity extends AppCompatActivity {
    private AutoCompleteTextView breedAutoComplete;
    private List<String> breedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_breeds_info);

        breedAutoComplete = findViewById(R.id.breedAutoComplete);
        breedAutoComplete.setThreshold(1);
        breedAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    breedAutoComplete.setText("");
                }
            }
        });

        // Make a network call to get the list of dog breeds from the server
        String url = "http://192.168.1.212:8091/dogBreed/breedsList"; // Replace with your actual API endpoint
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // process the response
                        List<String> breedList = parseBreedNames(response);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_dropdown_item_1line, breedList);
                        breedAutoComplete.setAdapter(adapter);
                        breedAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedBreed = (String) parent.getItemAtPosition(position);
                                Intent intent = new Intent(dogBreedsInfoActivity.this, dogInfoActivity.class);
                                intent.putExtra("breedName", selectedBreed);
                                startActivity(intent);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error getting breeds list from server", error);
                    }
                });
        // Add the request to the RequestQueue
        queue.add(stringRequest);


    }

    private List<String> parseBreedNames(String jsonResponse) {
        List<String> breedNames = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                String breedName = jsonArray.getString(i);
                breedName = breedName.replaceAll("\"", ""); // remove quotation marks
                breedNames.add(breedName);
            }
        } catch (JSONException e) {
            Log.e("JSON", "Error parsing JSON response", e);
        }
        return breedNames;
    }


}