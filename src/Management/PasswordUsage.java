package Management;

import DataSecurity.SecurityManager;

import javax.swing.*;

public class PasswordUsage {

    private final String username;
    private final String password;
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

//    public CharSequence getConfirmPassword() {
//        // Return confirm password
//        return newPassword;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public void viewPassword() {
        String password = getPassword();
        String encryptedPassword = SecurityManager.hashPassword(password);
        String username = getUsername();

        // Display the password in the format: password - [encrypted password] (username)
        JOptionPane.showMessageDialog(null, "Password - [" + encryptedPassword + "] (" + username + ")");
    }

    public void viewGeneratedPassword() {
        String generatedPassword = getNewPassword();
        String username = getUsername();

        // Display the generated password in the format: username: [generated password]
        JOptionPane.showMessageDialog(null, username + ": [" + generatedPassword + "]");
    }

}
