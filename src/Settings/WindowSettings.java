package Settings;

import javax.swing.*;
import java.awt.*;

import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

public class WindowSettings {
    private static final Font TITLE_FONT = new Font("Arial", BOLD, 24);
    private static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 18);

    public static void main(String[] args) {
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
}
