package SearchGoogleMaps;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VetFinder {
    private static final String API_KEY = "AIzaSyCPqo-LUmpUtxnQgCUIwEtYBjOyEbn6xuE";

    public static String getVetLocations(double client_latitude, double client_longitude) throws Exception {
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + client_latitude + "," + client_longitude + "&radius=5000&type=veterinary_care&key=" + API_KEY;

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

/*
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;

public class VetFinder {

    private static final String API_KEY = "AIzaSyBhHStktUHYybFGqFkSDVrAlewDdD9uFq0";

    public static String getVetLocations(double client_latitude, double client_longitude) throws InterruptedException, ApiException, IOException {
        // Set up the GeoApiContext with your API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        // Set the client's location
        LatLng location = new LatLng(client_latitude, client_longitude);

        // Define the search parameters
        NearbySearchRequest request = new NearbySearchRequest(context)
                .location(location)
                .radius(5000) // 5km radius
                .type(PlaceType.VETERINARY_CARE);

        PlacesSearchResponse response = request.await();
        // Perform the search and get the results
        StringBuilder sb = new StringBuilder();
        if (response.results.length == 0) {
            sb.append("No veterinarians found within 5km");
        } else {
            // Build a response string containing the names, locations, phone numbers, and websites of the veterinarians
            for (PlacesSearchResult result : response.results) {
                sb.append("Name: ").append(result.name).append("\n");
                sb.append("Location: ").append(result.geometry.location.toString()).append("\n");
                sb.append("Address: ").append(result.vicinity).append("\n");
                sb.append("Rating: ").append(result.rating).append("\n");
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
*/
