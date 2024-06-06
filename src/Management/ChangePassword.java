package Management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Settings.WindowSettings;

public class ChangePassword extends WindowSettings.GUIComponent {

    private boolean passwordDeleted = false;

    public ChangePassword(String title) {
        super(title);
        setSize(400, 300); // Override size for this specific window
        setLocationRelativeTo(null); // Center window on the screen

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 50));
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        JTextField oldPasswordField = new JTextField(15);
        JLabel newPasswordLabel = new JLabel("New Password:");
        final JTextField[] newPasswordField = {new JTextField(15)};
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        final JTextField[] confirmPasswordField = {new JTextField(15)};
        JButton deletePasswordButton = new JButton("Delete Password");
        JButton submitButton = new JButton("Submit");

        panel.add(oldPasswordLabel);
        panel.add(oldPasswordField);
        panel.add(newPasswordLabel);
        panel.add(newPasswordField[0]);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField[0]);
        panel.add(deletePasswordButton);
        panel.add(submitButton);

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
            if (newPasswordField[0].getText().isEmpty()) {
                JOptionPane.showMessageDialog(ChangePassword.this, "Please enter a new password.");
                return;
            }
            // Check if passwords match
            if (!newPasswordField[0].getText().equals(confirmPasswordField[0].getText())) {
                JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                return;
            }
            // Proceed with password change
            JOptionPane.showMessageDialog(ChangePassword.this, "Password Changed Successfully");
        });

        // Add window listener to prevent closing without entering new password
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (passwordDeleted && newPasswordField[0].getText().isEmpty()) {
                    while(true){
                        int check = JOptionPane.showConfirmDialog(ChangePassword.this, "Please enter a new password before closing the window.", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                        if(check == JOptionPane.OK_OPTION){
                            //Check password if password are matching
                            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                                JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                            } else {
                                // Proceed with closing the window if a new password is entered and matched
                                dispose();
                                break; // Exit the loop
                            }
                        } else {
                            // User cancelled, do nothing or handle as needed
                            break; // Exit the loop
                        }
                    }
                } else {
                    // Close the window if no special conditions
                    dispose();
                }
            }
        });

        add(panel, BorderLayout.CENTER);
    }
}
