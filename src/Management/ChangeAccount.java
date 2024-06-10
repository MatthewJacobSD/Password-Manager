package Management;

import DataSecurity.Account_Database;
import DataSecurity.SecurityManager;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ChangeAccount extends WindowSettings.GUIComponent {
    private JTextField emailField;
    private JComboBox<String> usernameComboBox;

    public ChangeAccount() {
        super("Change Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        String[] labels = {"Email Address:", "Username:"};
        emailField = new JTextField(15);
        usernameComboBox = new JComboBox<>();
        JComponent[] fields = {emailField, usernameComboBox};

        WindowSettings.GUIComponent.initComponents(panel, gbc, labels, fields);

        JButton loadButton = createButton("Load Usernames", e -> loadUsernames());
        WindowSettings.GUIComponent.addComponentToPane(panel, loadButton, gbc, 0, labels.length, 1);

        JButton switchButton = createButton("Switch Account", e -> switchAccount());
        WindowSettings.GUIComponent.addComponentToPane(panel, switchButton, gbc, 1, labels.length, 1);

        getContentPane().add(panel);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void loadUsernames() {
        String email = emailField.getText();
        if (!SecurityManager.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return;
        }

        List<User> users = Account_Database.readAccounts();
        usernameComboBox.removeAllItems();

        users.stream()
                .filter(user -> user.getEmail().equals(email))
                .forEach(user -> usernameComboBox.addItem(user.getUsername()));
    }

    private void switchAccount() {
        String selectedUsername = (String) usernameComboBox.getSelectedItem();
        if (selectedUsername != null) {
            UserAuth.currentUsername = selectedUsername;
            JOptionPane.showMessageDialog(this, "Switched to account: " + selectedUsername);
            dispose();
            new Menu("Menu");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a username.");
        }
    }

}
