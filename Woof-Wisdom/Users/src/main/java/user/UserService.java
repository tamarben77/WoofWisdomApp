package user;

import ManagmentDB.MySQLConnector;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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

    public boolean isTokenExpired(String token) {
        LocalDateTime tokenExpiration = MySQLConnector.getTokenExpiration(token);
        if (tokenExpiration == null) {
            // Handle error (e.g., token not found in the database)
            return true; // default to expired if not found
        }
        return tokenExpiration.isBefore(LocalDateTime.now());
    }


    public void updateUserPassword(String email, String newPassword) {
        // Use a connection to the database and update the password for the user with the given email
        try (Connection conn = MySQLConnector.getConnection()) {
            String sql = "UPDATE users SET Password = ? WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
    }
    public void invalidateToken(String token) {
        // Delete or invalidate the token in the `password_reset_tokens` table (or however you store the tokens)
        try (Connection conn = MySQLConnector.getConnection()) {
            String sql = "DELETE FROM password_reset_tokens WHERE token = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
    }

    public void sendResetEmail(String email, String resetLink) {
        // Use an email library or service to send the reset link to the user's email
        // This could be JavaMail, SendGrid, Mailgun, etc. You'll have to set this up.
    }

}
