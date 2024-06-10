package Management;

import DataSecurity.Account_Database;
import DataSecurity.SecurityManager;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChangeAccount extends WindowSettings.GUIComponent {
    private JTextField emailField;
    private JComboBox<String> usernameComboBox;

    public ChangeAccount() {
        super("Change Account");
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout()); // Create a panel with GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // Create GridBagConstraints for layout

        String[] labels = {"Email Address:", "Username:"}; // Labels for fields
        emailField = new JTextField(15); // Email field
        usernameComboBox = new JComboBox<>(); // Username dropdown
        JComponent[] fields = {emailField, usernameComboBox}; // Array of components

        WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields); // Initialize components

        JButton loadButton = createButton("Load Usernames", e -> loadUsernames()); // Load button
        WindowSettings.GUIComponent.addComponentToPane(panel, loadButton, gbc, 0, labels.length, 1); // Add load button

        JButton switchButton = createButton("Switch Account", e -> switchAccount()); // Switch button
        WindowSettings.GUIComponent.addComponentToPane(panel, switchButton, gbc, 1, labels.length, 1); // Add switch button

        getContentPane().add(panel); // Add panel to content pane
        setVisible(true); // Set visibility
    }

    private void loadUsernames() {
        String email = emailField.getText(); // Get entered email
        if (!SecurityManager.isValidEmail(email)) { // Check if email is valid
            JOptionPane.showMessageDialog(this, "Please enter a valid email address."); // Show error message
            return;
        }

        List<User> users = Account_Database.readAccounts(); // Read accounts from database
        usernameComboBox.removeAllItems(); // Remove existing items from combo box

        users.stream()
                .filter(user -> user.getEmail().equals(email)) // Filter users by email
                .forEach(user -> usernameComboBox.addItem(user.getUsername())); // Add usernames to combo box
    }

    private void switchAccount() {
        String selectedUsername = (String) usernameComboBox.getSelectedItem(); // Get selected username
        if (selectedUsername != null) { // Check if username is selected
            UserAuth.currentUsername = selectedUsername; // Update current username
            JOptionPane.showMessageDialog(this, "Switched to account: " + selectedUsername); // Show confirmation message
            dispose(); // Close current window
            new Menu(); // Open menu window
        } else {
            JOptionPane.showMessageDialog(this, "Please select a username."); // Show error message
        }
    }
}
