package DogFoodRequests;

import ManagmentDB.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcraft.jsch.JSchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DogFoodQuery {

    public DogFoodQuery() throws SQLException {
    }

    public static ResponseEntity showDogFoodCategories() throws SQLException, JsonProcessingException {
        List<Map<String, Object>> data = MySQLConnector.selectDistinct("foodCategory","dogFood");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // add the JSR-310 module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // configure to write dates as ISO-8601 strings
        return new ResponseEntity<>(mapper.writeValueAsString(data), HttpStatus.OK);
    }

    public static ResponseEntity showDogFoodItemsByCategory(String category_name) throws SQLException, JsonProcessingException, JSchException {

        List<Map<String, Object>> data = MySQLConnector.select("dogFood","foodCategory",category_name);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // add the JSR-310 module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // configure to write dates as ISO-8601 strings
        return new ResponseEntity<>(mapper.writeValueAsString(data), HttpStatus.OK);
    }

}
