package Management;

import Settings.WindowSettings;
import Database.Account_Database;
import Database.Keychain_Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class ChangePassword extends WindowSettings.GUIComponent {

    private boolean passwordDeleted = false;
    private final Password password;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ChangePassword(String title, Password password) {
        super(title);
        this.password = password;
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

        JButton deletePasswordButton = DeleteButton();

        JButton submitButton = SubmitButton();

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

    private JButton SubmitButton() {
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

            // Update the password in Account_Database and Keychain_Database
            ReadAccountsFiles(newPassword);
        });
        return submitButton;
    }

    private JButton DeleteButton() {
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
                    String newPassword = Arrays.toString(newPasswordField.getPassword());
                    password.setPassword(newPassword);

                    // Update the password in Account_Database and Keychain_Database
                    ReadAccountsFiles(newPassword);
                    break;
                }
            } else {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                break;
            }
        }
    }

    private void ReadAccountsFiles(String newPassword) {
        List<User> users = Account_Database.readAccounts();
        for (User user : users) {
            if (user.getUsername().equals(User_Auth.currentUsername)) {
                user.setPassword(newPassword);
                Account_Database.saveAccount(user);
                Keychain_Database.writePassword(user.getUsername(), newPassword, hashPassword(newPassword));
                break;
            }
        }

        JOptionPane.showMessageDialog(ChangePassword.this, "Password Changed Successfully");
        dispose();
    }

    // Add a method to hash the password (dummy implementation for this example)
    private String hashPassword(String password) {
        // Implement your hashing logic here
        return Integer.toString(password.hashCode());
    }
}
