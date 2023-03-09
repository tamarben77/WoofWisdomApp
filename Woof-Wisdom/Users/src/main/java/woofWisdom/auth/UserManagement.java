package woofWisdom.auth;

import org.apache.catalina.User;

public class UserManagement {
    private String username;
    private String password;

    public UserManagement() {}

    public UserManagement(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean validateCredentials(String username, String password) {
        // Replace with your own validation logic
        return "john.doe".equals(username) && "password123".equals(password);
    }

/*    public static User authenticateUser(String username, String password) {
        // Replace with your own authentication logic
        if ("john.doe".equals(username) && "password123".equals(password)) {
            return new User(1234, "John Doe", "john.doe@example.com");
        } else {
            return null;
        }
    }*/


}
