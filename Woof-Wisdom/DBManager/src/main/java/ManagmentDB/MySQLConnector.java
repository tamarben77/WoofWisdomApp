package ManagmentDB;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.Collections;


public class MySQLConnector {
/*
    private static final String DB_URL = "jdbc:MySQL://localhost/shakira";//"jdbc:mysql://localhost:3306/WoofWisdomDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "AAAaaa123";
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return conn;
    }
*/
    private static final String SSH_USER = "ubuntu";
    private static final String SSH_KEY_FILE = "ssh-keys/woofwisdomkey.pem";
    private static final String SSH_HOST = "ec2-184-72-117-35.compute-1.amazonaws.com";
    private static final int SSH_PORT = 22;
    private static final int DB_PORT = 3306;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "AAAaaa123";
    private static final String DB_NAME = "woofwisdomdb";

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

    public static void insertNewRow(String tableName, String[] columnNames, String[] values) {
        if (columnNames.length != values.length) {
            throw new IllegalArgumentException("Number of column names and values don't match");
        }
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO " + tableName + " (" + String.join(",", columnNames) + ") VALUES (" + String.join(",", Collections.nCopies(columnNames.length, "?")) + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                stmt.setString(i + 1, values[i]);
            }
            stmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE Email=? AND password=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set query parameters
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute query
            ResultSet rs = stmt.executeQuery();

            // Check if a record was found
            if (!rs.next()) {
                return false;
            }
            return true;
        } catch (SQLException | JSchException ex) {
            ex.printStackTrace();
            return false;
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


}
