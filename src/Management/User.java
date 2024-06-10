package Management;

public class User {
    private final String email;
    private final String username;
    private String password; // Changed to String type

    // Constructor for receiving password as a String
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Constructor for receiving password from PasswordUsage object
    public User(String email, String username, PasswordUsage passwordUsage) {
        this(email, username, passwordUsage.getPassword()); // Delegates to the other constructor
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
