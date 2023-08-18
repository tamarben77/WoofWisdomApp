package ManagmentDB;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MySQLConnector {

    private static final String SSH_USER = "ubuntu";

    //IMPORTANT - this location is only for local debugging
    //TODO - when deploying the server, this should be in a comment / removed
    private static final String SSH_KEY_FILE = "ssh-keys/woofWisdomKey.pem";

    //IMPORTANT - the second SSH-FILE-KEY's location is for the ec2 instance,
    // so it should be used only when running remote server
    //TODO - activate this configuration when deploying the server
    //private static final String SSH_KEY_FILE = "/home/ubuntu/woofWisdomKey.pem";
    private static final String SSH_HOST = "ec2-13-49-49-27.eu-north-1.compute.amazonaws.com";
    private static final int SSH_PORT = 22;
    private static final int DB_PORT = 3306;
    private static final String DB_USER = "woof";
    private static final String DB_PASSWORD = "AAAaaa123";
    private static final String DB_NAME = "woofwisdom";

    public static Connection getConnection() throws SQLException, JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(SSH_KEY_FILE);
        Session session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        int assignedPort = session.setPortForwardingL(0, "localhost", DB_PORT);
        String dbUrl = "jdbc:mysql://localhost:" + assignedPort + "/" + DB_NAME;
        Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
        return conn;
    }

    public static void insertNewRow(String tableName, String[] columnNames, Object[] values) {
        if (columnNames.length != values.length) {
            throw new IllegalArgumentException("Number of column names and values don't match");
        }
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO " + tableName + " (" + String.join(",", columnNames) + ") VALUES (" + String.join(",", Collections.nCopies(columnNames.length, "?")) + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                stmt.setString(i + 1, values[i].toString());
            }
            stmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Map<String, Object>> getTable(String tableName) {
        List<Map<String, Object>> data = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData meta = rs.getMetaData();
            int numColumns = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = meta.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // handle exception
        } catch (JSchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return data;
    }

    public static int getMaxId(String tableName, String columnName){
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(" + columnName + ") AS max_id FROM " + tableName;
            ResultSet maxId = stmt.executeQuery(query);
            int currId = 1;
            if (maxId.next()) {
                currId = maxId.getInt("max_id") + 1;
            }
            return currId;
        }catch(SQLException | JSchException ex){
            ex.printStackTrace();
            return 1;
        }
    }

    public static String checkCredentials(String username, String password) {
        // First check if the email exists
        String emailQuery = "SELECT * FROM users WHERE Email=?";
        try (Connection conn = getConnection();
             PreparedStatement emailStmt = conn.prepareStatement(emailQuery)) {

            emailStmt.setString(1, username);
            ResultSet rsEmail = emailStmt.executeQuery();

            // If email doesn't exist, return specific error
            if (!rsEmail.next()) {
                return "invalid_email";
            }

            // If email exists, check for correct password
            String passwordQuery = "SELECT * FROM users WHERE Email=? AND password=?";
            PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery);

            passwordStmt.setString(1, username);
            passwordStmt.setString(2, password);

            ResultSet rsPassword = passwordStmt.executeQuery();

            // If no record found with the given password, return specific error
            if (!rsPassword.next()) {
                return "invalid_password";
            }

            // If both email and password are correct, return valid
            return "valid";

        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            return "error"; // represent unknown errors
        }
    }


    public static int checkCredentials1(String username, String password) {
        String query = "SELECT * FROM users WHERE Email=? AND password=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set query parameters
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute query
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int numColumns = meta.getColumnCount();
            // Check if a record was found
            if (rs.next()) {
                int id=0;
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = meta.getColumnName(i);
                    if(columnName.equals("userID")) {
                        id = rs.getInt(i);
                    }
                }
                return id;
            }
            return 0;
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static boolean checkIfUserExists(String email) {
        boolean userExists = false;
        try (Connection conn = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                userExists = true;
            }
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
        return userExists;
    }

    public static List<Map<String, Object>> select(String tableName, String condition, String whereValue) throws SQLException, JSchException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM " + "woofwisdom." +tableName;
            if (condition != null) {
                sql += " WHERE " + condition + "=" + "'" +whereValue+ "'";
            }
            stmt = conn.prepareStatement(sql);
            System.out.println("Executing SQL query: " + sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                result.add(row);
            }
        } catch (SQLException | JSchException ex) {
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return result;
    }

    public static List<Map<String, Object>> selectDistinct(String columnName1,String tableName) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT DISTINCT "+columnName1+" FROM " + "woofwisdom." +tableName;
//            if (condition != null) {
//                sql += " WHERE " + condition + "=" + "'" +whereValue+ "'";
//            }
            stmt = conn.prepareStatement(sql);
            System.out.println("Executing SQL query: " + sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                result.add(row);
            }
        } catch (SQLException | JSchException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return result;
    }

    public static LocalDateTime getTokenExpiration(String token) {
        LocalDateTime expirationTime = null;

        try (Connection conn = getConnection()) {
            String sql = "SELECT expiration_time FROM tokens WHERE token = ?"; // Assuming your table name is "tokens" and the column name is "expiration_time"
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("expiration_time");
                expirationTime = timestamp.toLocalDateTime();
            }
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
        return expirationTime;
    }

    public static void storeTokenInDB(String email, String token, LocalDateTime expirationTime) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE sessions SET SessionId = ?, expiration_date  = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            stmt.setTimestamp(2, Timestamp.valueOf(expirationTime));  // Convert LocalDateTime to Timestamp
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static boolean isTokenExpired(String token) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT expiration_date FROM session WHERE SessionId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp expirationTimestamp = rs.getTimestamp("EXPERTION_DATE");
                LocalDateTime expirationTime = expirationTimestamp.toLocalDateTime();
                return expirationTime.isBefore(LocalDateTime.now());
            }
            return true;  // Token not found, hence consider it expired
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static String getEmailFromToken(String token) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT email FROM sessions WHERE SessionId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
            return null;  // No email found for this token
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static String getSessionIdByEmail(String email) {
        String sessionId = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT SessionId FROM sessions WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sessionId = rs.getString("SESSIONID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // For simplicity; consider proper logging or throwing a custom exception.
        }

        return sessionId;
    }

    public static void incrementCommentCountForQuestion(int questionId) {
        try (Connection conn = getConnection()) {
            // Assuming the table for questions is called "questions" and the column for comment count is "comment_count"
            String sql = "UPDATE forums SET comment_count = comment_count + 1 WHERE question_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, questionId);
            int affectedRows = stmt.executeUpdate();

            if(affectedRows == 0) {
                throw new SQLException("Failed to increment comment count. No rows affected.");
            }

            System.out.println("Comment count incremented successfully for question ID: " + questionId);
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static Map<Integer, Integer> getCommentCountsForAllQuestions() {
        Map<Integer, Integer> resultMap = new HashMap<>();

        try (Connection conn = getConnection()) {
            String sql = "SELECT question_id, COUNT(*) as comment_count FROM comments GROUP BY question_id";  // Replace 'comments' and 'question_id' with actual table and column names.
            PreparedStatement stmt = conn.prepareStatement(sql);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int questionId = rs.getInt("question_id");
                    int commentCount = rs.getInt("comment_count");
                    resultMap.put(questionId, commentCount);
                }
            }
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }

        return resultMap;
    }



}
