package Management;

import DataSecurity.Account_Database;
import DataSecurity.Keychain_Database;
import DataSecurity.SecurityManager;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.Menu;
import java.util.List;

import static DataSecurity.SecurityManager.isPasswordGenerated;
import static DataSecurity.SecurityManager.isValidEmail;

public class UserAuth {
    public static String currentUsername; // Current logged-in username
    public static String currentPassword; // Current logged-in password

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new); // Start the login window
    }

    // Login window for user authentication
    public static class Login extends WindowSettings.GUIComponent {
        // Constructor to initialize the Login window
        public Login() {
            super("Login"); // Set the title of the window
            initComponents(); // Initialize GUI components
        }

        // Method to initialize GUI components
        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout()); // Create a panel with GridBagLayout
            GridBagConstraints gbc = new GridBagConstraints(); // Create GridBagConstraints

            JLabel loginTitleLabel = new JLabel("Account: " + currentUsername);
            String[] labels = {"Username:", "Password:"}; // Array of label texts
            JTextField usernameField = new JTextField(15); // Username text field
            JPasswordField passwordField = new JPasswordField(15); // Password field
            JComponent[] fields = {usernameField, passwordField}; // Array of input fields

            // Add the title label at the top
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10); // Add padding around the label
            gbc.anchor = GridBagConstraints.CENTER; // Center the title label
            panel.add(loginTitleLabel, gbc);

            // Add the username and password fields
            WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields);

            // Create and add Login button to the panel
            JButton loginButton = createButton("Login", e -> {
                String username = usernameField.getText(); // Get username from text field
                String password = new String(passwordField.getPassword()); // Get password from password field

                if (validateLogin(username, password)) { // Validate login credentials
                    currentUsername = username; // Set current username
                    currentPassword = password; // Set current password
                    dispose();// Close the current window
                    SwingUtilities.invokeLater(() -> new Management.Menu("Menu"));// Initialize the Menu window after successful login
                }else {
                    // Show error message for invalid login credentials
                    JOptionPane.showMessageDialog(null, "Invalid login credentials. Please try again.");
                }
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, loginButton, gbc, 0, labels.length + 1, 1); // Add Login button

            // Create and add Register button to the panel
            JButton registerButton = createButton("Register here!", e -> {
                dispose(); // Close the current window
                new Register(); // Open the registration window
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, registerButton, gbc, 1, labels.length + 1, 1); // Add Register button

            JButton viewGeneratedPasswordButton = createButton("View Generated Password", e -> {
                PasswordUsage passwordUsage = new PasswordUsage(currentUsername, currentPassword);
                passwordUsage.viewGeneratedPassword();
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, viewGeneratedPasswordButton, gbc, 2, labels.length + 1, 1);

            getContentPane().add(panel); // Add panel to the content pane of the window
            setVisible(true); // Set the window visible
        }

        // Method to validate login credentials
        private boolean validateLogin(String username, String password) {
            if (username.isEmpty() || password.isEmpty()) { // Check if username or password is empty
                return false;
            }
            List<User> users = Account_Database.readAccounts(); // Read user accounts from database
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return true; // Return true if a matching user is found
                }
            }
            return false; // Return false if no matching user is found
        }
    }

    // Registration window for new user registration
    public static class Register extends WindowSettings.GUIComponent {
        public Register() {
            super("Register"); // Set title to Register
            initComponents();
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout

            JLabel registerTitleLabel = new JLabel("Account Registration");
            String[] labels = {"Email Address:", "Username:", "Password:", "Confirm Password:"};
            JTextField emailField = new JTextField(15);
            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);
            JPasswordField confirmPasswordField = new JPasswordField(15);
            JComponent[] fields = {emailField, usernameField, passwordField, confirmPasswordField};

            // Add the title label at the top
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10); // Add padding around the label
            gbc.anchor = GridBagConstraints.CENTER; // Center the title label
            panel.add(registerTitleLabel, gbc);

            WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields);

            // Create and add Register button to the panel
            JButton registerButton = createButton("Confirm", e -> {
                String email = emailField.getText(); // Get email from text field
                String username = usernameField.getText(); // Get username from text field
                String password = new String(passwordField.getPassword()); // Get password from password field
                String confirmPassword = new String(confirmPasswordField.getPassword()); // Get confirm password from password field

                if (validateRegistration(email, username, password, confirmPassword)) {// Validate registration details
                    User user = new User(email, username, password); // Create User object with password as a String
                    if (Account_Database.saveAccount(user)) {
                        Keychain_Database.writePassword(username, password, SecurityManager.hashPassword(password), SecurityManager.generateRandomPassword(15)); // Write password details to keychain
                        // Save user account to database
                        isPasswordGenerated("", "");
                        // Show registration success message
                        JOptionPane.showMessageDialog(null, "Registration successful. Welcome " + username);
                        dispose(); // Close the current window
                        new Login(); // Open the login window
                    } else {
                        // Show error message for saving user data
                        JOptionPane.showMessageDialog(null, "Error saving user data. Please try again.");
                    }
                } else {
                    // Show error message for invalid registration details
                    JOptionPane.showMessageDialog(null, "Invalid registration details. Please ensure all fields are correctly filled.");
                }
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, registerButton, gbc, 0, labels.length + 1, 1); // Add Register button

            // Login Button
            JButton loginButton = createButton("Already Registered?", e -> {
                dispose();
                new Login();
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, loginButton, gbc, 1, labels.length + 1, 1);

            // Generate Password Button
            JButton generatePasswordButton = createButton("Generate Password", e -> {
                String generatedPassword = SecurityManager.generateRandomPassword(15);
                passwordField.setText(generatedPassword);
                confirmPasswordField.setText(generatedPassword);
            });
            WindowSettings.GUIComponent.addComponentToPane(panel, generatePasswordButton, gbc, 2, labels.length + 1, 1);

            getContentPane().add(panel); // Add content to panel
            setVisible(true); // Make the window visible
        }

        // Method to validate registration details
        private boolean validateRegistration(String email, String username, String password, String confirmPassword) {
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                return false; // Return false if any field is empty
            }
            if (!isValidEmail(email)) {// Show error message for invalid email format
                JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                return false;
            }
            if (SecurityManager.isnotPasswordStrong(password)) {// Show error message for invalid password format
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long, contain an uppercase letter and a special character.");
                return false;
            }
            if (!password.equals(confirmPassword)) {// Show error message if passwords do not match
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return false;
            }
            List<User> users = Account_Database.readAccounts(); // Read user accounts from database
            return users.stream().noneMatch(user -> user.getEmail().equals(email)); // Check if email is not already registered
        }
    }
}
