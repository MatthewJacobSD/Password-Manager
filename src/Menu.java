import Management.ChangeAccount;
import Management.ChangePassword;
import Management.PasswordUsage;
import Management.UserAuth;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;

public class Menu extends WindowSettings.GUIComponent {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menuWindow = new Menu("Menu");
            menuWindow.setVisible(true);
        });
    }

    public Menu(String title) {
        super(title);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Dashboard");
        JLabel subtitleLabel = new JLabel("Welcome to the dashboard, " + UserAuth.currentUsername);
        JOptionPane.showMessageDialog(null, "-- Welcome to the Dashboard -- \n --Team 6--");

        WindowSettings.GUIComponent.addComponentToPane(panel, titleLabel, gbc, 0, 0, 2);
        WindowSettings.GUIComponent.addComponentToPane(panel, subtitleLabel, gbc, 0, 1, 2);

        getContentPane().add(panel);
        applyFonts(getContentPane());

        initializeMenu();
        setVisible(true); // Make sure the window is visible
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Left-aligned menus
        JMenu aboutMenu = new JMenu("About");

        // Add contributors submenu
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

        // Add left-aligned menu first
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
            new UserAuth.Login(); // Open the login window
        });

        accountMenu.add(changeAccount);
        accountMenu.add(changePassword);
        accountMenu.add(logOut);

        return accountMenu;
    }
}
