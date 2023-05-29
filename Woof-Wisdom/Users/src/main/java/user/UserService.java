package user;

import ManagmentDB.MySQLConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class UserService {

    private final MySQLConnector mySQLConnector;

    public UserService() {
        this.mySQLConnector = new MySQLConnector();
    }

    public String getUserFirstNameFromEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT First_Name FROM users WHERE Email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("First_Name");
        }
        return null;
    }

    public String getUserLastNameFromEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT Last_Name FROM users WHERE Email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("Last_Name");
        }
        return null;
    }
}
