package Management;

import Settings.WindowSettings;
import Database.Account_Database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class User_Auth {
    public static void main(String[] args) {
        new Login(); // Start the application with the login page
    }

    // User Access
    private static class Login extends WindowSettings.GUIComponent {
        public Login() {
            super("Login"); // Set title to Log in
            initComponents();
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout

            String[] labels = {"Username:", "Password:"};
            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);
            JComponent[] fields = {usernameField, passwordField};

            WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields);

            // Login Button
            JButton loginButton = createButton("Login", e -> {
                // Action performed when the login button is clicked
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                Password passwordObj = new Password(password, password);
                PasswordUsage passwordUsage = new PasswordUsage(passwordObj);

                // Validate login credentials
                if (validateLogin(username, passwordUsage)) {
                    new Menu(); // Open dashboard if login is successful
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials. Please try again.");
                }
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, loginButton, gbc, 0, labels.length, 1);

            // Register button
            JButton registerButton = createButton("Register", e -> {
                new Register(); // Open register GUI when clicked
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, registerButton, gbc, 1, labels.length, 1);

            getContentPane().add(panel); // Add content to panel
            setVisible(true); // Make the window visible
        }

        // Method to validate login credentials
        private boolean validateLogin(String username, PasswordUsage passwordUsage) {
            if (username.isEmpty() || passwordUsage.getPassword().getPassword().isEmpty()) {
                return false;
            }
            List<User> users = Account_Database.readAccounts(); // Read users from file
            return users.stream().anyMatch(user ->
                    user.getUsername().equals(username) &&
                            Password.hashPassword(passwordUsage.getPassword().getPassword()).equals(user.getPassword())
            );
        }
    }

    // Registration Section
    private static class Register extends WindowSettings.GUIComponent {
        public Register() {
            super("Register"); // Set title to Register
            initComponents();
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout

            String[] labels = {"Email Address:", "Username:", "Password:", "Confirm Password:"};
            JTextField emailField = new JTextField(15);
            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);
            JPasswordField confirmPasswordField = new JPasswordField(15);
            JComponent[] fields = {emailField, usernameField, passwordField, confirmPasswordField};

            WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields);

            // Register Button
            JButton registerButton = createButton("Register", e -> {
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                Password passwordObj = new Password(password, confirmPassword);
                PasswordUsage passwordUsage = new PasswordUsage(passwordObj);

                if (validateRegistration(email, username, passwordUsage)) {
                    User user = new User(username, email, Password.hashPassword(password));
                    if (Account_Database.saveAccount(user)) {
                        JOptionPane.showMessageDialog(null, "Registration successful. Welcome " + username);
                        dispose();
                        new Menu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error saving user data. Please try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid registration details. Please ensure all fields are correctly filled.");
                }
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, registerButton, gbc, 0, labels.length, 1);

            // Login Button
            JButton loginButton = createButton("Already Registered?", e -> {
                dispose();
                new Login();
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, loginButton, gbc, 1, labels.length, 1);

            getContentPane().add(panel); // Add content to panel
            setVisible(true); // Make the window visible
        }

        private boolean validateRegistration(String email, String username, PasswordUsage passwordUsage) {
            if (email.isEmpty() || username.isEmpty() || passwordUsage.getPassword().getNewPassword().isEmpty() || passwordUsage.getPassword().getConfirmPassword().isEmpty()) {
                return false;
            }
            if (!Account_Database.isValidEmail(email)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                return false;
            }
            if (!passwordUsage.getPassword().isValidPassword(passwordUsage.getPassword().getNewPassword())) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long, contain an uppercase letter and a special character.");
                return false;
            }
            if (!passwordUsage.getPassword().getNewPassword().equals(passwordUsage.getPassword().getConfirmPassword())) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return false;
            }
            List<User> users = Account_Database.readAccounts();
            return users.stream().noneMatch(user -> user.getEmail().equals(email));
        }
    }
}
