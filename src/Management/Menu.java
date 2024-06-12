package Management;

import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;

public class Menu extends WindowSettings.GUIComponent {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu("Menu"));// Start the menu window
    }

    public Menu(String title) {
        super(title);
        initializeComponents();
        initializeMenu();
        setVisible(true); // Make sure the window is visible
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Dashboard");
        JLabel subtitleLabel = new JLabel("Welcome to the dashboard, " + UserAuth.currentUsername);

        // Display welcome message
        JOptionPane.showMessageDialog(null, "-- Welcome to the Dashboard -- \n-- Team 6 --");

        // Add components to the panel
        WindowSettings.GUIComponent.addComponentToPane(panel, titleLabel, gbc, 0, 0, 2);
        WindowSettings.GUIComponent.addComponentToPane(panel, subtitleLabel, gbc, 0, 1, 2);

        getContentPane().add(panel);
        applyFonts(getContentPane());

        JButton viewGeneratedPasswordButton = createButton("View Password", e -> {
            PasswordUsage passwordUsage = new PasswordUsage(UserAuth.currentUsername, UserAuth.currentPassword);
            passwordUsage.viewPassword();
        });
        WindowSettings.GUIComponent.addComponentToPane(panel, viewGeneratedPasswordButton, gbc, 0, 2, 2);

        getContentPane().add(panel);
        applyFonts(getContentPane());
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Left-aligned menus
        JMenu aboutMenu = new JMenu("About");
        JMenu contributorsMenu = new JMenu("Contributors");
        String[] contributors = {
                "Lead project: Findlay",
                "Front-end developers: Findlay, Stefano, Matthew",
                "Back-end developers: Stefano, Matthew",
                "Project Manager: Findlay, Ash",
                "Testers: Everyone"
        };
        for (String contributor : contributors) {
            contributorsMenu.add(new JMenuItem(contributor));
        }
        aboutMenu.add(contributorsMenu);
        menuBar.add(aboutMenu);

        // Create glue to push the next menus to the right
        menuBar.add(Box.createHorizontalGlue());

        // Right-aligned menus
        JMenu accountMenu = getAccountMenu();
        menuBar.add(accountMenu);

        setJMenuBar(menuBar);

    }
    private JMenu getAccountMenu() {
        JMenu accountMenu = new JMenu("Account");

        JMenuItem changeAccount = new JMenuItem("Change Account");
        changeAccount.addActionListener(e -> {
            ChangeAccount changeAccountFrame = new ChangeAccount();
            changeAccountFrame.setVisible(true);
        });

        JMenuItem changePassword = new JMenuItem("Change Password");
        changePassword.addActionListener(e -> {
            ChangePassword changePasswordFrame = new ChangePassword("Change Password", new PasswordUsage(UserAuth.currentUsername, UserAuth.currentPassword));
            changePasswordFrame.setVisible(true);
        });

        JMenuItem logOut = new JMenuItem("Log out");
        logOut.addActionListener(e -> {
            dispose(); // Close the current window
            SwingUtilities.invokeLater(UserAuth.Login::new); // Open the login window
        });

        accountMenu.add(changeAccount);
        accountMenu.add(changePassword);
        accountMenu.add(logOut);

        return accountMenu;
    }

}