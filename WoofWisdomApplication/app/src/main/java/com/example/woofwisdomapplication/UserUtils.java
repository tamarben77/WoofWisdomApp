package com.example.woofwisdomapplication;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUtils {

    public static void displayWelcomeMessage(Context context, TextView welcomeTextView) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sessionId = sharedPreferences.getString("sessionID", null);

        if (sessionId != null) {
            String url = BASE_URL + "auth/getUserInfo?sessionID=" + sessionId;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String firstName = response.getString("firstName");
                                String lastName = response.getString("lastName");
                                String welcomeMessage = "Welcome, " + firstName + " " + lastName + "!";
                                welcomeTextView.setText(welcomeMessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            int timeout = 60000;
            request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(request);
        }
    }
}
