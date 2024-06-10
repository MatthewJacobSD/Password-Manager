package Management;

public class PasswordUsage {

    private final String username;
    private String password;
    private String newPassword; // Variable to store the new password

    public PasswordUsage(String username, String currentPassword) {
        this.username = username;
        this.password = currentPassword;
    }

    public String getUsername() {
        return String.valueOf(username); // Convert username to string
    }

    public String getPassword(){
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public CharSequence getConfirmPassword() {
        // Return confirm password
        return newPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
