package user;

import ManagmentDB.MySQLConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import javax.websocket.Session;

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

    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public static void sendPasswordResetEmail(String email, String resetToken) {
        // Configure your email settings (SMTP server, credentials, etc.)
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "your-smtp-host");
        properties.put("mail.smtp.port", "your-smtp-port");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@example.com", "your-email-password");
            }
        });

        try {
            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your-email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset Request");
            message.setText("Please click on the following link to reset your password: " +
                    "http://your-website.com/resetPassword?token=" + resetToken);

            // Send the message
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }




}
