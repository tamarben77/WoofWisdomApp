package vaccinations;

import ManagmentDB.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VaccinationsManager {

    public VaccinationsManager() throws SQLException {
    }

    public static ResponseEntity showAllVaccinations() throws SQLException, JsonProcessingException {
        String json = getTableDataJson("vaccinations");
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    public static String getTableDataJson(String tableName) throws JsonProcessingException {
        List<Map<String, Object>> data = MySQLConnector.getTable(tableName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // add the JSR-310 module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // configure to write dates as ISO-8601 strings
        return mapper.writeValueAsString(data);
    }

    public static void addNewVaccinationRecord(String username, String vac_name, String vac_date, String vac_description, String vac_location) throws SQLException {
        String[] columnNames = {"vaccination_name", "username", "date", "description", "location"};
        String[] values = {vac_name, username, vac_date, vac_description, vac_location};
        MySQLConnector.insertNewRow("vaccinations", columnNames, values);
    }
}
