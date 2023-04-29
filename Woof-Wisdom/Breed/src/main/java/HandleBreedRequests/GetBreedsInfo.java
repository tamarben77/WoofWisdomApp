package HandleBreedRequests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

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
    public  static DogObject getBreedInfoByName(String responseBody, String breedName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        int index = 0;
        while (index < responseJson.size()) {
            if(breedName.equals(responseJson.get(index).get("breedName").asText())){
                return new DogObject(breedName,responseJson.get(index).get("breedType").asText(),responseJson.get(index).get("breedDescription").asText(), responseJson.get(index).get("furColor").asText(),
                        responseJson.get(index).get("origin").asText(), responseJson.get(index).get("minHeightInches").asText(), responseJson.get(index).get("maxHeightInches").asText(),responseJson.get(index).get("minWeightPounds").asText(),responseJson.get(index).get("maxWeightPounds").asText(),responseJson.get(index).get("maxLifeSpan").asText());
            }
            index++;
        }
        return null;

    }
}