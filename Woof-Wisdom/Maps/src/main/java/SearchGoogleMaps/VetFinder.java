package SearchGoogleMaps;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VetFinder {
    //private static final String API_KEY = "AIzaSyCPqo-LUmpUtxnQgCUIwEtYBjOyEbn6xuE";
    //TODO - check that the new api key works
    private static final String API_KEY = "AIzaSyC9K2MxvVTHmwPEeLCX2V2zidoTfMaqK8s";

    public static String getVetLocations(Double client_latitude, Double client_longitude, int radius) throws Exception {
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + client_latitude + "," + client_longitude + "&radius=" + radius + "&type=veterinary_care&key=" + API_KEY;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();

        br.close(); // Close the input stream

        conn.disconnect();

        JsonObject jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
        JsonArray resultsArray = jsonObject.getAsJsonArray("results");

        JsonArray simplifiedResults = new JsonArray();
        for (JsonElement element : resultsArray) {
            try {
                JsonObject result = element.getAsJsonObject();
                JsonObject simplifiedResult = new JsonObject();
                simplifiedResult.addProperty("name", result.get("name").getAsString());
                simplifiedResult.addProperty("vicinity", result.get("vicinity").getAsString());
                if(result.get("rating") != null) {
                    simplifiedResult.addProperty("rating", result.get("rating").getAsFloat());
                }
                simplifiedResults.add(simplifiedResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        conn.disconnect();

        JsonObject simplifiedJson = new JsonObject();
        simplifiedJson.add("results", simplifiedResults);
        simplifiedJson.addProperty("status", "OK");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJsonString = gson.toJson(simplifiedJson);

        return prettyJsonString;
    }
}
