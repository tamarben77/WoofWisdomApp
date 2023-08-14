package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.oldMainActivity.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.CacheManager.NetworkUtils;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class forumsAddQuestion extends AppCompatActivity {
    MaterialSpinner whichForum;
    private static final String URL = BASE_URL+"dogForums/createNewQuery";
    ProgressDialog progressDialog;
    EditText name;
    EditText title;
    EditText details;
    Button send_btn;
    String selected_food="Select Option";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums_add_question);

        name = (EditText) findViewById(R.id.name);
        details = (EditText) findViewById(R.id.details);
        title = (EditText) findViewById(R.id.title);

        /* Food Spinner :- */
        whichForum = (MaterialSpinner) findViewById(R.id.whichForum);
        whichForum.setItems("Select Option","User", "Vet", "Trainers", "Dog Siters");
        whichForum.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selected_food=item;
                /* SnackBar.make(view, "Clicked " + item, SnackBar.LENGTH_INDEFINITE).show(); */
            }
        });


        send_btn = (Button) findViewById(R.id.button);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().equals(""))
                {
                    name.setError("Please enter value");
                }else if(title.getText().equals(""))
                {
                    title.setError("Please enter value");
                }else if(selected_food.equals("Select Option"))
                {
                    whichForum.setError("Please select value");
                }else if(details.getText().equals(""))
                {
                    details.setError("Please enter value");
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("ifNewQuery",1);
                    requestBody.put("questionTitle",title.getText().toString());
                    requestBody.put("questionDetails",details.getText().toString());
                    requestBody.put("userID",sharedPreferences.getInt("userID",0));
                    requestBody.put("userType","");
                    requestBody.put("upvotes",0);
                    requestBody.put("views",0);
                    requestBody.put("category",selected_food);

                    String jsonBody = new Gson().toJson(requestBody);
                    JSONObject jsonRequestBody = null;
                    try {
                        jsonRequestBody = new JSONObject(jsonBody);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    progressDialog =new ProgressDialog(forumsAddQuestion.this);
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,jsonRequestBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(), "Question added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    updateForumsData();
                                    progressDialog.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Question added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    progressDialog.dismiss();

                                }
                            });

                    int timeout = 60000;
                    request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    Volley.newRequestQueue(getApplicationContext()).add(request);

                }
            }
        });


    }

    private void updateForumsData(){
        fetchDataFromServer(BASE_URL + "dogForums/showAllForumsPost", "all_forums");
    }
    private void fetchDataFromServer(String serverUrl, final String cacheKey) {
        NetworkUtils.fetchDataAsync(this, serverUrl, new NetworkUtils.DataCallback() {
            @Override
            public void onDataFetched(String data) {
                // Data has been fetched and stored in the cache with the given key
            }
        });
    }

}