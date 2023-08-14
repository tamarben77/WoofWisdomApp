package com.example.woofwisdomapplication.CacheManager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.common.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public interface DataCallback {
        void onDataFetched(String data);
    }

    public static void fetchDataAsync(Context context, String url, DataCallback callback) {
        new FetchDataTask(context, url, callback).execute();
    }

    private static class FetchDataTask extends AsyncTask<Void, Void, String> {
        private final Context context;
        private final String url;
        private final DataCallback callback;

        FetchDataTask(Context context, String url, DataCallback callback) {
            this.context = context;
            this.url = url;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\A");
                if (scanner.hasNext()) {
                    return scanner.next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (data != null) {
                CacheManager cacheManager = new CacheManager(context);
                Type dataType = new TypeToken<String>(){}.getType(); // Change this type accordingly
                cacheManager.saveData(url, data, dataType);
                callback.onDataFetched(data);
            }
        }
    }
}

