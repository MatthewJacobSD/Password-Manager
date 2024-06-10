import Management.ChangePassword;
import Management.PasswordUsage;
import Management.UserAuth;
import Settings.WindowSettings;

import javax.swing.*;
import java.awt.*;

import static Management.UserAuth.currentUsername;

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
        JOptionPane.showMessageDialog(null, "-- Welcome to the Dashboard -- \n --Team 6--");

        addComponentToPane(panel, titleLabel, gbc, 0, 0, 2);

        getContentPane().add(panel);
        applyFonts(getContentPane());

        initializeMenu();
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

    private static JMenu getAccountMenu() {
        JMenu accountMenu = new JMenu("Account");
        JMenuItem changeAccount = new JMenuItem("Change Account");
        JMenuItem changePassword = currentDetails();
        JMenuItem logOut = new JMenuItem("Log out");
        logOut.addActionListener(e -> System.exit(0));

        accountMenu.add(changeAccount);
        accountMenu.add(changePassword);
        accountMenu.add(logOut);

        return accountMenu;
    }

    private static JMenuItem currentDetails() {
        String currentPassword = UserAuth.currentPassword;
        JMenuItem changePassword = new JMenuItem("Change Password");
        changePassword.addActionListener(e -> {
            ChangePassword changePasswordFrame = new ChangePassword("Change Password", new PasswordUsage(currentUsername, currentPassword));
            changePasswordFrame.setVisible(true);
        });
        return changePassword;
    }
}
