package Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Management.ChangePassword;

import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

public class WindowSettings {
    private static final Font TITLE_FONT = new Font("Arial", BOLD, 24);
    private static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 18);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExampleWindow exampleWindow = new ExampleWindow("Example Window");
            exampleWindow.setVisible(true);
        });
    }

    // Window Setup
    public abstract static class GUIComponent extends JFrame {
        protected GUIComponent(String title) {
            setTitle(title); // Set Title
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close the window
            setSize(700, 500); // Set Window size
            setLocationRelativeTo(null); // Center window on the screen
            applyFonts(this.getContentPane());
        }

        protected void addComponentToPane(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y, int width) {
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = width;
            gbc.insets = new Insets(10, 10, 10, 10); // Padding
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
            panel.add(component, gbc);
        }

        protected void applyFonts(Container container) {
            for (Component component : container.getComponents()) {
                if (component instanceof JLabel label) {
                    if (getTitle().equals(label.getText())) {
                        label.setFont(TITLE_FONT);
                    } else {
                        label.setFont(DEFAULT_FONT);
                    }
                } else if (component instanceof Container) {
                    applyFonts((Container) component);
                }
            }
        }
    }

    // Example GUI Component for demonstration
    private static class ExampleWindow extends GUIComponent {
        public ExampleWindow(String title) {
            super(title);
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel titleLabel = new JLabel("Example Window");
            JLabel regularLabel = new JLabel("This is a regular label");
            JButton clickMeButton = new JButton("Click Me");
            clickMeButton.addActionListener(new ButtonClickListener());

            addComponentToPane(panel, titleLabel, gbc, 0, 0, 2);
            addComponentToPane(panel, regularLabel, gbc, 0, 1, 2);
            addComponentToPane(panel, clickMeButton, gbc, 0, 2, 2);

            getContentPane().add(panel);
            applyFonts(getContentPane());

            initializeMenu();
        }

        private void initializeMenu() {
            JMenuBar menuBar = new JMenuBar();

            // Left-aligned menus
            JMenu fileMenu = new JMenu("File");
            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.addActionListener(e -> System.exit(0));
            fileMenu.add(exitItem);
            menuBar.add(fileMenu);

            // Add a horizontal glue to push the following menus to the right
            menuBar.add(Box.createHorizontalGlue());

            // Right-aligned menus
            JMenu accountMenu = new JMenu("Account");
            JMenuItem changeAccount = new JMenuItem("Change Account");
            JMenuItem changePassword = new JMenuItem("Change Password");
            changePassword.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangePassword changePasswordFrame = new ChangePassword("Change Password");
                    changePasswordFrame.setVisible(true);
                }
            });
            JMenuItem logOut = new JMenuItem("Log out");
            logOut.addActionListener(e -> System.exit(0));

            accountMenu.add(changeAccount);
            accountMenu.add(changePassword);
            accountMenu.add(logOut);

            JMenu aboutMenu = new JMenu("About");
            JMenuItem contributorsItem = new JMenuItem("Contributors");
            aboutMenu.add(contributorsItem);

            menuBar.add(accountMenu);
            menuBar.add(aboutMenu);

            setJMenuBar(menuBar);
        }

        private static class ButtonClickListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Button clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
