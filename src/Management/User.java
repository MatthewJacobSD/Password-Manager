package Management;

public class User {
    private final String email;
    private final String username;
    private String password;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; // Simply return the password
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
