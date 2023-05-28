package ManagmentDB;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.*;


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
    private static final String SSH_HOST = "ec2-174-129-143-139.compute-1.amazonaws.com";
    private static final int SSH_PORT = 22;
    private static final int DB_PORT = 3306;
    private static final String DB_USER = "root";
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

    public static int checkCredentials(String username, String password) {
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

    public static List<Map<String, Object>> select(String tableName, String condition, String whereValue) throws SQLException {
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
        } catch (SQLException ex) {
            throw ex;
        } catch (JSchException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException ex) {
            throw ex;
        } catch (JSchException e) {
            throw new RuntimeException(e);
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



}
