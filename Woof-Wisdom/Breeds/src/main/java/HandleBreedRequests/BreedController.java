package HandleBreedRequests;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/dogBreed")
public class BreedController {
    private static final String API_BASE_URL = "https://dogbreeddb.p.rapidapi.com/";
    private static final HttpHeaders API_HEADERS = new HttpHeaders();

    static {
        API_HEADERS.set("X-RapidAPI-Key", "cd055cd225msh68221a6ffec8ad8p11f191jsn20e1c61f85c9");
        API_HEADERS.set("X-RapidAPI-Host", "dogbreeddb.p.rapidapi.com");
    }
    @GetMapping("/breedsList")
    public ResponseEntity<?> breeds() throws JsonProcessingException {
        String url = API_BASE_URL;
        ResponseEntity<String> response = getDataFromApi(url);
        List<String> responseBody = GetBreedsInfo.extractBreedNames(response.getBody());
        // do something with the response
        return ResponseEntity.ok(responseBody);
    }
    @GetMapping("/breedsInfo/{breedName}")
    public ResponseEntity<?> breedInfo(@PathVariable String breedName) throws JsonProcessingException {
        String url = API_BASE_URL;
        ResponseEntity<String> response = getDataFromApi(url);
        String responseBody = response.getBody(); // get the response body
        DogObject requestedDog  = GetBreedsInfo.getBreedInfoByName(responseBody, breedName);
        return ResponseEntity.ok(requestedDog);
    }
    private ResponseEntity<String> getDataFromApi(String url) {
        HttpEntity<String> entity = new HttpEntity<>(API_HEADERS);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
