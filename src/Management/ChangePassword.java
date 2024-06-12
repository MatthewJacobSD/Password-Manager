package Management;

import DataSecurity.Account_Database;
import DataSecurity.Keychain_Database;
import DataSecurity.SecurityManager;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class ChangePassword extends WindowSettings.GUIComponent {

    private boolean passwordDeleted = false;
    private final PasswordUsage passwordUsage;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ChangePassword(String title, PasswordUsage passwordUsage) {
        super(title);
        this.passwordUsage = passwordUsage;
        setSize(400, 300);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        oldPasswordField = new JPasswordField(15);
        newPasswordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JLabel("Old Password:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(oldPasswordField, gbc);
        gbc.gridy++;
        panel.add(newPasswordField, gbc);
        gbc.gridy++;
        panel.add(confirmPasswordField, gbc);
        gbc.gridy++;

        JButton deletePasswordButton = createDeleteButton();
        JButton submitButton = createSubmitButton();

        gbc.gridx = 0;
        panel.add(deletePasswordButton, gbc);
        gbc.gridx = 1;
        panel.add(submitButton, gbc);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (passwordDeleted && newPasswordField.getPassword().length == 0) {
                    promptForNewPassword();
                } else {
                    dispose();
                }
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    private JButton createSubmitButton() {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            char[] newPasswordChars = newPasswordField.getPassword();
            char[] confirmPasswordChars = confirmPasswordField.getPassword();

            if (newPasswordChars.length == 0) {
                JOptionPane.showMessageDialog(ChangePassword.this, "Please enter a new password.");
                return;
            }
            if (!Arrays.equals(newPasswordChars, confirmPasswordChars)) {
                JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                return;
            }
            String newPassword = new String(newPasswordChars); // Convert char array to String

            // Validate the old password before updating
            if (!SecurityManager.verifyPassword(UserAuth.currentUsername, new String(oldPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Old password is incorrect.");
                return;
            }

            // Validate the new password strength
            if (SecurityManager.isnotPasswordStrong(newPassword)) {
                JOptionPane.showMessageDialog(this, "New password is not strong enough. It must be at least 8 characters long, contain an uppercase letter, and a special character.");
                return;
            }

            // Update the password in Account_Database and Keychain_Database
            updatePassword(newPassword);
        });
        return submitButton;
    }

    private JButton createDeleteButton() {
        JButton deletePasswordButton = new JButton("Delete Password");
        deletePasswordButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(ChangePassword.this, "Are you sure you want to delete the password?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                passwordDeleted = true;
                oldPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
                JOptionPane.showMessageDialog(ChangePassword.this, "Password Deleted Successfully");
            }
        });
        return deletePasswordButton;
    }

    private void promptForNewPassword() {
        while (true) {
            int check = JOptionPane.showConfirmDialog(ChangePassword.this, "Please enter a new password before closing the window.", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (check == JOptionPane.OK_OPTION) {
                if (!Arrays.equals(newPasswordField.getPassword(), confirmPasswordField.getPassword())) {
                    JOptionPane.showMessageDialog(ChangePassword.this, "New password and confirm password do not match.");
                } else {
                    String newPassword = new String(newPasswordField.getPassword());

                    // Validate the new password strength
                    if (SecurityManager.isnotPasswordStrong(newPassword)) {
                        JOptionPane.showMessageDialog(this, "New password is not strong enough. It must be at least 8 characters long, contain an uppercase letter, and a special character.");
                        return;
                    }

                    updatePassword(newPassword);
                    break;
                }
            } else {
                dispose();
                break;
            }
        }
    }

    private void updatePassword(String newPassword) {
        String username = passwordUsage.getUsername();

        // Hash the new password
        String hashedPassword = SecurityManager.hashPassword(newPassword);

        // Update the password in Account_Database and Keychain_Database
        if (Account_Database.updateAccountPassword(username, hashedPassword) &&
                Keychain_Database.updatePassword(username, hashedPassword)) {
            JOptionPane.showMessageDialog(this, "Password updated successfully.");
            UserAuth.currentPassword = newPassword;
            dispose();
            new Menu("Menu");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.");
        }
    }
}
