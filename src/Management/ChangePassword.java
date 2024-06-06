package Management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Settings.WindowSettings;

public class ChangePassword extends WindowSettings.GUIComponent {

    private boolean passwordDeleted = false;
    private final JTextField newPasswordField;
    private final JTextField confirmPasswordField;
    private final Password password;

    public ChangePassword(String title, Password password) {
        super(title);
        this.password = password;
        setSize(400, 300); // Override size for this specific window
        setLocationRelativeTo(null); // Center window on the screen

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the components
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        JTextField oldPasswordField = new JTextField(15);
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JTextField(15); // Assign to the class variable
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JTextField(15); // Assign to the class variable
        JButton deletePasswordButton = new JButton("Delete Password");
        JButton submitButton = new JButton("Submit");

        panel.add(oldPasswordLabel, gbc);
        gbc.gridx++;
        panel.add(oldPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(newPasswordLabel, gbc);
        gbc.gridx++;
        panel.add(newPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(confirmPasswordLabel, gbc);
        gbc.gridx++;
        panel.add(confirmPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span the buttons across two columns
        panel.add(deletePasswordButton, gbc);
        gbc.gridy++;


        deletePasswordButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(ChangePassword.this, "Are you sure you want to delete the password?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                passwordDeleted = true;
                oldPasswordField.setText(""); // Clear the old password field
                JOptionPane.showMessageDialog(ChangePassword.this, "Password Deleted Successfully");
            }
        });

        submitButton.addActionListener(e -> {
            // Implement password change logic here
            // Check if new password is entered
            if (password.getNewPassword().isEmpty()) { // Access newPasswordField from Password object
                JOptionPane.showMessageDialog(ChangePassword.this, "Please enter a new password.");
                return;
            }
            // Check if passwords match
            if (!password.getNewPassword().equals(password.getConfirmPassword())) { // Access confirmPasswordField from Password object
                JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                return;
            }
            // Proceed with password change
            password.setPassword(password.getNewPassword());
            JOptionPane.showMessageDialog(ChangePassword.this, "Password Changed Successfully");
            dispose();
        });

        // Add window listener to prevent closing without entering new password
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (passwordDeleted && password.getNewPassword().isEmpty()) { // Access newPasswordField from Password object
                    promptForNewPassword();
                } else {
                    dispose();
                }
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    private void promptForNewPassword() {
        while (true) {
            int check = JOptionPane.showConfirmDialog(ChangePassword.this, "Please enter a new password before closing the window.", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (check == JOptionPane.OK_OPTION) {
                // Check if passwords match
                if (!password.getNewPassword().equals(password.getConfirmPassword())) { // Access confirmPasswordField from Password object
                    JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                } else {
                    // Proceed with closing the window if a new password is entered and matched
                    password.setPassword(password.getNewPassword());
                    JOptionPane.showMessageDialog(ChangePassword.this, "Password Changed Successfully");
                    dispose();
                    break; // Exit the loop
                }
            } else {
                // User cancelled, prevent window from closing
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                break; // Exit the loop
            }
        }
    }
}
