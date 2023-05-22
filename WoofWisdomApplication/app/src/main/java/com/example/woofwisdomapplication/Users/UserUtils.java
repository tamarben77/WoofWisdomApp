package com.example.woofwisdomapplication.Users;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUtils {

    /*public static class MyUserObjectListener implements UserObjectListener {
        private UserObject userObject;
        private UserObjectCallback callback;

        public void setCallback(UserObjectCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onUserObjectReceived(UserObject userObject) {
            this.userObject = userObject;
            if (callback != null) {
                callback.onUserObjectReady(userObject);
            }
        }

        @Override
        public void onError(VolleyError error) {
            // Handle the error here
            // You can choose to handle errors differently if needed
            error.printStackTrace();
            if (callback != null) {
                callback.onError(error);
            }
        }

        public interface UserObjectCallback {
            void onUserObjectReady(UserObject userObject);
            void onError(VolleyError error);
        }
    }*/

    public static void displayWelcomeMessage(Context context, TextView welcomeTextView) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sessionId = sharedPreferences.getString("sessionID", null);

        if (sessionId != null) {
            String url = "http://192.168.1.212:8091/auth/getUserInfo?sessionID=" + sessionId;

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

  /*  public static UserObject getUserObjectBySessionID(Context context, UserObjectListener listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sessionId = sharedPreferences.getString("sessionID", null);
        String url = "http://192.168.1.212:8091/auth/getUserInfo?sessionID=" + sessionId;
        RequestQueue queue = Volley.newRequestQueue(context);
        final UserObject[] userObject = {null};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserObject userObject = parseUserObjectFromResponse(response);
                        listener.onUserObjectReceived(userObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                });

        int timeout = 60000;
        request.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return userObject[0];
    }

    private static UserObject parseUserObjectFromResponse(JSONObject response) {
        UserObject userObject = new UserObject();

        try {
            userObject.setSessionId(response.getString("sessionId"));
            userObject.setEmail(response.getString("email"));
            userObject.setFirstName(response.getString("firstName"));
            userObject.setLastName(response.getString("lastName"));
            userObject.setDogName(response.optString("dogName", null));
            userObject.setDogWeight(response.optInt("dogWeight", 0));
            userObject.setDogAge(response.optInt("dogAge", 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userObject;
    }




*/
}
