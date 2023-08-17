package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.Adapters.FoodAdapter;
import com.example.woofwisdomapplication.CacheManager.CacheManager;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.views.FoodMeasureActivity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {
    private static final String URL = BASE_URL + "showDogFoodCategories";
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    List<FoodCategoryModel> food_list;
    GridLayoutManager layoutManager;
    FoodAdapter myAdapter;
    Button button;
    private CacheManager cacheManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Setting up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cacheManager = new CacheManager(this);
        button = (Button) findViewById(R.id.measure_food);

        /* ArrayList :- */
        food_list = new ArrayList<>();

        Map<String, String> requestBody = new HashMap<>();

        String jsonBody = new Gson().toJson(requestBody);
        JSONObject jsonRequestBody = null;
        try {
            jsonRequestBody = new JSONObject(jsonBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.food_fragment);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        myAdapter = new FoodAdapter(food_list, getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);

        Type dataType = new TypeToken<String>() {
        }.getType();
        String cachedData = cacheManager.getData("food_categories", dataType);
        if (cachedData != null) {
            handleData(cachedData);
        } else {
            // Fetch data from the server and save it in the cache
            fetchDataFromServer();
        }

        /* Measure Food Activity :- */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FoodMeasureActivity.class));
            }
        });
    }

    private void handleData(String response) {
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                food_list.add(new FoodCategoryModel(FoodActivity.this, array.getJSONObject(i).getString("foodCategory")));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        progressDialog.dismiss();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void fetchDataFromServer() {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mubi", response.toString());
                        handleData(response);
                        Type dataType = new TypeToken<String>(){}.getType();
                        cacheManager.saveData("food_categories", response.toString(), dataType);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                });

        int timeout = 60000;
        request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            // Handle "Home" click here, maybe go to the main activity or dashboard
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_return) {
            // Handle "Return" click, maybe just close the current activity
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}