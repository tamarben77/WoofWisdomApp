package HandleBreedRequests;

import ManagmentDB.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jcraft.jsch.JSchException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBreedsInfo {
    public static List<String> extractBreedNames(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        List<String> breedNames = new ArrayList<>();
        for (int i = 0; i < responseJson.size(); i++) {
            breedNames.add(responseJson.get(i).get("breedName").asText());
        }
        return breedNames;
    }
    public static DogObject getBreedInfoByName(String responseBody, String breedName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        int index = 0;
        while (index < responseJson.size()) {
            if (breedName.equals(responseJson.get(index).get("breedName").asText())) {
                // Retrieve additional breed information from the database
                String adaptability = "";
                String healthAndGrooming = "";
                String trainability = "";
                String exerciseNeeds = "";
                String friendliness = "";

                try (Connection conn = MySQLConnector.getConnection()) {
                    String selectQuery = "SELECT adaptability, health_and_grooming, trainability, exercise_needs, friendliness " +
                            "FROM breed_info WHERE breed_name = ?";
                    PreparedStatement stmt = conn.prepareStatement(selectQuery);
                    stmt.setString(1, breedName);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        adaptability = rs.getString("adaptability");
                        healthAndGrooming = rs.getString("health_and_grooming");
                        trainability = rs.getString("trainability");
                        exerciseNeeds = rs.getString("exercise_needs");
                        friendliness = rs.getString("friendliness");
                    }
                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    // handle database error
                }

                return new DogObject(breedName, responseJson.get(index).get("breedType").asText(),
                        responseJson.get(index).get("breedDescription").asText(), responseJson.get(index).get("furColor").asText(),
                        responseJson.get(index).get("origin").asText(), responseJson.get(index).get("minHeightInches").asText(),
                        responseJson.get(index).get("maxHeightInches").asText(), responseJson.get(index).get("minWeightPounds").asText(),
                        responseJson.get(index).get("maxWeightPounds").asText(), responseJson.get(index).get("maxLifeSpan").asText(),
                        adaptability, healthAndGrooming, trainability, exerciseNeeds, friendliness);
            }
            index++;
        }
        return null;
    }
}