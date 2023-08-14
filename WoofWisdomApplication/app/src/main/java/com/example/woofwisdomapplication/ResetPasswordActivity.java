package com.example.woofwisdomapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);
        Button submitButton = findViewById(R.id.buttonSubmitNewPassword);

        Uri data = getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = getIntent().getDataString();
            final String token = Uri.parse(uri).getQueryParameter("token");

            // Handle the button click
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPassword = newPasswordEditText.getText().toString();
                    resetPassword(token, newPassword);
                }
            });
        }
    }

    private void resetPassword(String token, String newPassword) {
        String IP = "YOUR_SERVER_IP";  // replace with your server's IP address
        String url = "http://" + IP + ":8091/auth/resetPassword";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle server's response
                        Toast.makeText(ResetPasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(ResetPasswordActivity.this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("newPassword", newPassword);
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(ResetPasswordActivity.this).add(postRequest);
    }
}

