package com.example.woofwisdomapplication.views;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.Adapters.FoodDetailsAdapter;
import com.example.woofwisdomapplication.FoodActivity;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.data.model.FoodModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private static final String URL = BASE_URL+"showDogFoodItemsByCategory";
    ProgressDialog progressDialog;
    LinearLayoutManager layoutManager;
    List<FoodModel> food_details_list;
    FoodDetailsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        recyclerView=(RecyclerView) findViewById(R.id.food_details);

        String food_category=getIntent().getStringExtra("category");

        /* ArrayList :- */
        food_details_list=new ArrayList<>();

        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, URL+"?category_name="+food_category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mubi",response.toString());
                        try {
                            JSONArray array=new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {
                                food_details_list.add(new FoodModel(FoodDetailsActivity.this,array.getJSONObject(i).getString("foodName"),array.getJSONObject(i).getString("foodInfo"),array.getJSONObject(i).getString("effectsIfHealthy"),array.getJSONObject(i).getString("effectsIfDangerous"),array.getJSONObject(i).getString("foodCategory"),array.getJSONObject(i).getInt("ifDangerous"),array.getJSONObject(i).getInt("ifHealthy")));
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


        recyclerView= findViewById(R.id.food_details);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myAdapter=new FoodDetailsAdapter(food_details_list,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}