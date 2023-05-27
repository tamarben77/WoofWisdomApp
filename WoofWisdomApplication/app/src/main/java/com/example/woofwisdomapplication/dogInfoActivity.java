package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class dogInfoActivity extends AppCompatActivity {
    String selectedBreed;
    private static final String IP = System.getProperty("IP");
    private static final String URL = "http://" + IP + "/dogBreed/breedsInfo/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_breed_info);

        // Get the selected breed name from the intent
        selectedBreed = getIntent().getStringExtra("breedName");
        String url = URL + selectedBreed;

        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        String breedName = selectedBreed;
        String imageName = "breed_" + breedName.toLowerCase();
        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resourceId != 0) {
            mainLayout.setBackgroundResource(resourceId);
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String breedName = jsonObject.getString("breedName");
                            String breedDescription = jsonObject.getString("breedDescription");
                            String breedType = jsonObject.getString("breedType");
                            String furColor = jsonObject.getString("furColor");
                            String origin = jsonObject.getString("origin");
                            TextView nameTextView = findViewById(R.id.breed_name_value_textview);
                            TextView descriptionTextView = findViewById(R.id.breed_description_value_textview);
                            TextView breedTypeTextView = findViewById(R.id.breed_type_value_textview);
                            TextView furColorTextView = findViewById(R.id.fur_color_value_textview);
                            TextView originTextView = findViewById(R.id.origin_value_textview);
                            nameTextView.setText(breedName);
                            descriptionTextView.setText(breedDescription);
                            furColorTextView.setText(furColor);
                            breedTypeTextView.setText(breedType);
                            originTextView.setText(origin);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        queue.add(stringRequest);
    }
}
