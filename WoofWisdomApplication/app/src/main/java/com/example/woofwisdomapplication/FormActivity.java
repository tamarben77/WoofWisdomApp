package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.Adapters.ForumAdapter;
import com.example.woofwisdomapplication.CacheManager.CacheManager;
import com.example.woofwisdomapplication.DTO.Vaccination;
import com.example.woofwisdomapplication.data.model.ForumModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class FormActivity extends AppCompatActivity {
    RecyclerView recyclerview_list;
    private static final String URL = BASE_URL+"dogForums/showAllForumsPost";
    ProgressDialog progressDialog;
    List<ForumModel> forum_list,copy_list;
    LinearLayoutManager layoutManager;
    ForumAdapter myAdapter;
    FloatingActionButton floatingActionButton;
    EditText searchEdt;
    private CacheManager cacheManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        cacheManager = new CacheManager(this);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.floatingActionButton);
        searchEdt=(EditText) findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                myAdapter.filter(s.toString());
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
               if(sharedPreferences.getString("sessionID","").equals(""))
               {
                   Toast.makeText(FormActivity.this,"You need to login first",Toast.LENGTH_SHORT).show();
               }else{
                   startActivity(new Intent(getApplicationContext(), forumsAddQuestion.class));
               }


            }
        });

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

        forum_list = new ArrayList<>();
        copy_list = new ArrayList<>();

        recyclerview_list =(RecyclerView) findViewById(R.id.list);
        //recyclerview_list.getAdapter().notifyDataSetChanged();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        myAdapter=new ForumAdapter(forum_list,copy_list,this);
        recyclerview_list.setAdapter(myAdapter);
        recyclerview_list.setLayoutManager(layoutManager);
        Type dataType = new TypeToken<String>(){}.getType();
        String cachedData = cacheManager.getData("all_forums", dataType);
        if (cachedData != null) {
            handleData(cachedData);
            myAdapter.notifyDataSetChanged(); // Move this line here
        } else {
            // Fetch data from the server and save it in the cache
            fetchDataFromServer();
        }

        Log.d("list",""+forum_list.size());

    }
    private void handleData(String response){
        try {
            JSONArray array=new JSONArray(response);
            for(int i=0;i<array.length();i++)
            {
                forum_list.add(new ForumModel(FormActivity.this,array.getJSONObject(i).getInt("ifNewQuery"),array.getJSONObject(i).getString("questionTitle"),array.getJSONObject(i).getString("questionDetails"),array.getJSONObject(i).getInt("userID"),array.getJSONObject(i).getString("userType"),array.getJSONObject(i).getInt("upvotes"),array.getJSONObject(i).getInt("views"),array.getJSONObject(i).getString("category"),array.getJSONObject(i).getInt("questionID"),array.getJSONObject(i).getString("dateandTime")));
                copy_list.add(new ForumModel(FormActivity.this,array.getJSONObject(i).getInt("ifNewQuery"),array.getJSONObject(i).getString("questionTitle"),array.getJSONObject(i).getString("questionDetails"),array.getJSONObject(i).getInt("userID"),array.getJSONObject(i).getString("userType"),array.getJSONObject(i).getInt("upvotes"),array.getJSONObject(i).getInt("views"),array.getJSONObject(i).getString("category"),array.getJSONObject(i).getInt("questionID"),array.getJSONObject(i).getString("dateandTime")));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        progressDialog.dismiss();
    }

    private void fetchDataFromServer(){
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mubi",response.toString());
                        handleData(response);

                        Type dataType = new TypeToken<String>(){}.getType();
                        cacheManager.saveData("all_forums", response.toString(), dataType);
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
}