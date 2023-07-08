package com.example.woofwisdomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
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
    DBHelper dbHelper;
    private static final String IP = System.getProperty("IP");
    private static final String URL = "http://" + IP + ":8091/dogBreed/breedsInfo/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_breed_info);
        // Get the selected breed name from the intent
        selectedBreed = getIntent().getStringExtra("breedName");
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { // attributes you want to retrieve
                "breedName",
                "breedDescription",
                "breedType",
                "furColor",
                "origin",
                "adaptability",
                "healthAndGrooming",
                "trainability",
                "exerciseNeeds",
                "friendliness",
        };
        String selection = "breedName = ?";
        String[] selectionArgs = {selectedBreed};
        Cursor cursor = db.query(
                "breedInfo",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        String url = null;
        if (cursor.moveToFirst()) {
            do {
                int breedDescriptionIndex = cursor.getColumnIndex("breedDescription");
                String breedDescription = breedDescriptionIndex != -1 ? cursor.getString(breedDescriptionIndex) : null;
                int breedTypeIndex = cursor.getColumnIndex("breedType");
                String breedType = breedTypeIndex != -1 ? cursor.getString(breedTypeIndex) : null;
                int breedNameIndex = cursor.getColumnIndex("breedName");
                String breedName = breedNameIndex != -1 ? cursor.getString(breedNameIndex) : null;
                int furColorIndex = cursor.getColumnIndex("furColor");
                String furColor = furColorIndex != -1 ? cursor.getString(furColorIndex) : null;
                int originIndex = cursor.getColumnIndex("origin");
                String origin = originIndex != -1 ? cursor.getString(originIndex) : null;
                int adaptabilityIndex = cursor.getColumnIndex("adaptability");
                String adaptability = adaptabilityIndex != -1 ? cursor.getString(adaptabilityIndex) : null;
                int healthAndGroomingIndex = cursor.getColumnIndex("adaptability");
                String healthAndGrooming = healthAndGroomingIndex != -1 ? cursor.getString(adaptabilityIndex) : null;
                int trainabilityIndex = cursor.getColumnIndex("trainability");
                String trainability = trainabilityIndex != -1 ? cursor.getString(trainabilityIndex) : null;
                int exerciseNeedsIndex = cursor.getColumnIndex("exerciseNeeds");
                String exerciseNeeds = exerciseNeedsIndex != -1 ? cursor.getString(exerciseNeedsIndex) : null;
                int friendlinessIndex = cursor.getColumnIndex("friendliness");
                String friendliness = friendlinessIndex != -1 ? cursor.getString(friendlinessIndex) : null;


                TextView descriptionTextView = findViewById(R.id.breed_description_value_textview);
                descriptionTextView.setText(breedDescription != null ? "About: " + breedDescription : "No description available");
                TextView breedTypeTextView = findViewById(R.id.breed_type_value_textview);
                breedTypeTextView.setText(breedType != null ? "Type: " + breedType : "No type available");
                TextView breedNameTextView = findViewById(R.id.breed_name_value_textview);
                breedNameTextView.setText(breedType != null ? "Type: " + breedName : "No type available");
                TextView furColorTextView = findViewById(R.id.fur_color_value_textview);
                furColorTextView.setText(furColor != null ? "Type: " + furColor : "No type available");
                TextView originTextView = findViewById(R.id.origin_value_textview);
                originTextView.setText(origin != null ? "Type: " + origin : "No type available");
                TextView adaptabilityTextView = findViewById(R.id.adaptability_textview);
                adaptabilityTextView.setText(adaptability != null ? "Type: " + adaptability : "No type available");
                TextView healthAndGroomingTextView = findViewById(R.id.health_and_grooming_textview);
                healthAndGroomingTextView.setText(healthAndGrooming != null ? "Type: " + healthAndGrooming : "No type available");
                TextView trainabilityTextView = findViewById(R.id.trainability_textview);
                trainabilityTextView.setText(trainability != null ? "Type: " + trainability : "No type available");
                TextView exerciseNeedsTextView = findViewById(R.id.exercise_needs_textview);
                exerciseNeedsTextView.setText(exerciseNeeds != null ? "Type: " + exerciseNeeds : "No type available");
                TextView friendlinessTextView = findViewById(R.id.friendliness_textview);
                friendlinessTextView.setText(friendliness != null ? "Type: " + friendliness : "No type available");

            } while (cursor.moveToNext());
        } else {
            url = URL + selectedBreed;
        }

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
                            String breedDescription = jsonObject.getString("breedDescription");
                            String breedType = jsonObject.getString("breedType");
                            String furColor = jsonObject.getString("furColor");
                            String origin = jsonObject.getString("origin");
                            String adaptability = jsonObject.getString("adaptability");
                            String healthAndGrooming = jsonObject.getString("healthAndGrooming");
                            String trainability = jsonObject.getString("trainability");
                            String exerciseNeeds = jsonObject.getString("exerciseNeeds");
                            String friendliness = jsonObject.getString("friendliness");

                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("breedName", selectedBreed);
                            values.put("breedDescription", breedDescription);
                            values.put("breedType", breedType);
                            values.put("furColor", breedType);
                            values.put("origin", breedType);
                            values.put("adaptability", breedType);
                            values.put("healthAndGrooming", breedType);
                            values.put("trainability", breedType);
                            values.put("exerciseNeeds", breedType);
                            values.put("friendliness", breedType);
                            long newRowId = db.insert("breedInfo", null, values);

                            TextView nameTextView = findViewById(R.id.breed_name_value_textview);
                            TextView descriptionTextView = findViewById(R.id.breed_description_value_textview);
                            TextView breedTypeTextView = findViewById(R.id.breed_type_value_textview);
                            TextView furColorTextView = findViewById(R.id.fur_color_value_textview);
                            TextView originTextView = findViewById(R.id.origin_value_textview);
                            TextView adaptabilityCaptionTextView = findViewById(R.id.adaptability_textview);
                            TextView healthAndGroomingCaptionTextView = findViewById(R.id.health_and_grooming_textview);
                            TextView trainabilityCaptionTextView = findViewById(R.id.trainability_textview);
                            TextView exerciseNeedsCaptionTextView = findViewById(R.id.exercise_needs_textview);
                            TextView friendlinessCaptionTextView = findViewById(R.id.friendliness_textview);

                            adaptabilityCaptionTextView.setText("Adaptability");
                            healthAndGroomingCaptionTextView.setText("Health and Grooming");
                            trainabilityCaptionTextView.setText("Trainability");
                            exerciseNeedsCaptionTextView.setText("Exercise Needs");
                            friendlinessCaptionTextView.setText("Friendliness");

                            nameTextView.setText(selectedBreed);
                            descriptionTextView.setText("About: " + breedDescription);
                            furColorTextView.setText("Four Color: " + furColor);
                            breedTypeTextView.setText("Type: " + breedType);
                            originTextView.setText("Origin" + origin);

                            RatingBar adaptabilityRatingBar = findViewById(R.id.adaptability_ratingbar);
                            RatingBar healthAndGroomingRatingBar = findViewById(R.id.health_and_grooming_ratingbar);
                            RatingBar trainabilityRatingBar = findViewById(R.id.trainability_ratingbar);
                            RatingBar exerciseNeedsRatingBar = findViewById(R.id.exercise_needs_ratingbar);
                            RatingBar friendlinessRatingBar = findViewById(R.id.friendliness_ratingbar);

                            if (Float.parseFloat(adaptability) > 0) {
                                adaptabilityRatingBar.setRating(Float.parseFloat(adaptability));
                                adaptabilityRatingBar.setVisibility(View.VISIBLE);
                            } else {
                                adaptabilityRatingBar.setVisibility(View.INVISIBLE);
                            }

                            // Set the rating value and visibility for health and grooming rating
                            if (Float.parseFloat(healthAndGrooming) > 0) {
                                healthAndGroomingRatingBar.setRating(Float.parseFloat(healthAndGrooming));
                                healthAndGroomingRatingBar.setVisibility(View.VISIBLE);
                            } else {
                                healthAndGroomingRatingBar.setVisibility(View.INVISIBLE);
                            }

                            // Set the rating value and visibility for trainability rating
                            if (Float.parseFloat(trainability) > 0) {
                                trainabilityRatingBar.setRating(Float.parseFloat(trainability));
                                trainabilityRatingBar.setVisibility(View.VISIBLE);
                            } else {
                                trainabilityRatingBar.setVisibility(View.INVISIBLE);
                            }

                            // Set the rating value and visibility for exercise needs rating
                            if (Float.parseFloat(exerciseNeeds) > 0) {
                                exerciseNeedsRatingBar.setRating(Float.parseFloat(exerciseNeeds));
                                exerciseNeedsRatingBar.setVisibility(View.VISIBLE);
                            } else {
                                exerciseNeedsRatingBar.setVisibility(View.INVISIBLE);
                            }

                            // Set the rating value and visibility for friendliness rating
                            if (Float.parseFloat(friendliness) > 0) {
                                friendlinessRatingBar.setRating(Float.parseFloat(friendliness));
                                friendlinessRatingBar.setVisibility(View.VISIBLE);
                            } else {
                                friendlinessRatingBar.setVisibility(View.INVISIBLE);
                            }

                            healthAndGroomingRatingBar.setRating(Float.parseFloat(healthAndGrooming));
                            trainabilityRatingBar.setRating(Float.parseFloat(trainability));
                            exerciseNeedsRatingBar.setRating(Float.parseFloat(exerciseNeeds));
                            friendlinessRatingBar.setRating(Float.parseFloat(friendliness));
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
