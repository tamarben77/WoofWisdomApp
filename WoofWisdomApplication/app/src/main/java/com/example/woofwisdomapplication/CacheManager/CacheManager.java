package com.example.woofwisdomapplication.CacheManager;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class CacheManager {

    private static final String CACHE_PREFS_NAME = "app_cache";
    private static final String KEY_PREFIX = "cache_";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CacheManager(Context context) {
        sharedPreferences = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public <T> void saveData(String key, T data, Type dataType) {
        String jsonData = gson.toJson(data, dataType);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PREFIX + key, jsonData);
        editor.apply();
    }

    public <T> T getData(String key, Type dataType) {
        String jsonData = sharedPreferences.getString(KEY_PREFIX + key, null);
        if (jsonData != null) {
            return gson.fromJson(jsonData, dataType);
        }
        return null;
    }
}
