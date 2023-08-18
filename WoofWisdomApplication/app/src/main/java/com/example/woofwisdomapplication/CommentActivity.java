package com.example.woofwisdomapplication;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static com.example.woofwisdomapplication.oldMainActivity.BASE_URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.Adapters.CommentsAdapter;
import com.example.woofwisdomapplication.data.model.CommentsModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private static final String URL = BASE_URL+"dogForums/showCommentsOfPost";
    private static final String URL1 = BASE_URL+"dogForums/createNewComment";
    ProgressDialog progressDialog;
    List<CommentsModel> comments_list;
    CommentsAdapter adapter;

    EditText comment;
    ImageView add_comment;
    int questionId_st;

    TextView commentCountTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        // Setting up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("inside","true");
        recyclerView = (RecyclerView) findViewById(R.id.user_comment);

        String title_st=getIntent().getStringExtra("title");
        String date_st=getIntent().getStringExtra("date");
        questionId_st=getIntent().getIntExtra("questionId",0);

        TextView title=findViewById(R.id.title);
        TextView date=findViewById(R.id.date);

        title.setText(title_st);
        date.setText(date_st);

        commentCountTextView = findViewById(R.id.comment_count);


        /* ArrayList :- */
        comments_list = new ArrayList<>();

        recyclerView= findViewById(R.id.user_comment);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        adapter=new CommentsAdapter(comments_list,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        add_comment = findViewById(R.id.add_comment);
        comment = findViewById(R.id.comment);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment.getText().toString().equals(""))
                {
                    comment.setError("Please enter the comment");
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    String[] columnNames = {"commentTitle",  "commentDesc", "userName", "userID", "questionID"};
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("commentTitle",comment.getText().toString());
                    requestBody.put("commentDesc","");
                    requestBody.put("userName","Demo User");
                    requestBody.put("userID",sharedPreferences.getInt("userID",0));
                    requestBody.put("questionID",questionId_st);

                    String jsonBody = new Gson().toJson(requestBody);
                    JSONObject jsonRequestBody = null;
                    try {
                        jsonRequestBody = new JSONObject(jsonBody);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    progressDialog =new ProgressDialog(CommentActivity.this);
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL1,jsonRequestBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                                    loadData(questionId_st);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                                    loadData(questionId_st);


                                }
                            });

                    int timeout = 60000;
                    request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(questionId_st);
    }

    void loadData(int questionId_st){
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

        // Fetching comment count
        String URL_COUNT = BASE_URL + "dogForums/getCommentCountsForAllQuestions";
        StringRequest countRequest = new StringRequest(Request.Method.GET, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int commentCount = jsonObject.getInt(String.valueOf(questionId_st));
                            commentCountTextView.setText(commentCount + " Comments");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error fetching comment count: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(getApplicationContext()).add(countRequest);

        // Fetching comments for the question
        comments_list.clear();
        StringRequest request = new StringRequest(Request.Method.GET, URL+"?questionId="+questionId_st,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mubi", response.toString());
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                comments_list.add(new CommentsModel(
                                        CommentActivity.this,
                                        array.getJSONObject(i).getString("commentTitle"),
                                        array.getJSONObject(i).getString("commentDesc"),
                                        array.getJSONObject(i).getString("userName"),
                                        array.getJSONObject(i).getInt("userID"),
                                        array.getJSONObject(i).getInt("questionID"),
                                        array.getJSONObject(i).getString("dateandTime"),
                                        array.getJSONObject(i).getInt("commentId")
                                ));
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
                }
        );

        int timeout = 60000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
    public ActionBar getSupportActionBar() {
        return null;
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
