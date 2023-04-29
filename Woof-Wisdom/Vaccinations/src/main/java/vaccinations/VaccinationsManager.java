package vaccinations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaccinationsManager {

    public VaccinationsManager() throws SQLException {
    }

    public static ResponseEntity showAllVaccinations() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shakira", "root", "Aaa123aaa");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM vaccinations");
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();

        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= numColumns; i++) {
                String columnName = rsmd.getColumnName(i);
                Object columnValue = rs.getObject(i);
                row.put(columnName, columnValue);
            }
            rows.add(row);
        }
        rs.close();
        stmt.close();
        conn.close();

        return new ResponseEntity<>(rows, HttpStatus.OK);
    }


    public static void addNewVaccinationRecord(String username, String vac_name, String vac_date, String vac_description, String vac_location) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shakira", "root", "Aaa123aaa");
        Statement stmt = conn.createStatement();
        ResultSet maxId = stmt.executeQuery("SELECT MAX(vaccination_id) AS max_id FROM vaccinations");
        int currId = 1;
        if (maxId.next()) {
            currId = maxId.getInt("max_id") + 1;
        }        /*currId = maxId.getInt(1);*/
        String query = String.format("INSERT INTO shakira.vaccinations (vaccination_id, vaccination_name, username, date, description, location) " +
                "VALUES (%d, '%s', '%s', CURDATE(), '%s', '%s')", currId, vac_name, username, vac_description, vac_location);
        int rowsAffected = stmt.executeUpdate(query);

        maxId.close();
        stmt.close();
        conn.close();
    }
}
