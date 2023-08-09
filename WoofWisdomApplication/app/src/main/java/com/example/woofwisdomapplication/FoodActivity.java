package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.Adapters.FoodAdapter;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.views.FoodMeasureActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {
    private static final String URL = BASE_URL+"showDogFoodCategories";
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    List<FoodCategoryModel>food_list;
    GridLayoutManager layoutManager;
    FoodAdapter myAdapter;
    Button button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        button=(Button)findViewById(R.id.measure_food);


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


        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      Log.d("mubi",response.toString());
                        try {
                            JSONArray array=new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {
                                food_list.add(new FoodCategoryModel(FoodActivity.this,array.getJSONObject(i).getString("foodCategory")));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        progressDialog.dismiss();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        int timeout = 60000;
        request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(request);

        recyclerView = (RecyclerView) findViewById(R.id.food_fragment);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        myAdapter=new FoodAdapter(food_list,getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);

       /* Measure Food Activity :- */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FoodMeasureActivity.class));
            }
        });
    }
}