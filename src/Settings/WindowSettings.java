package Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

public class WindowSettings {
    private static final Font TITLE_FONT = new Font("Arial", BOLD, 24);
    private static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 18);

    public static void main(String[] args) {
        // This main method can be used for testing if needed
    }

    public abstract static class GUIComponent extends JFrame {
        protected GUIComponent(String title) {
            setTitle(title);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(700, 500);
            setLocationRelativeTo(null);
            applyFonts(this.getContentPane());
        }

        protected static void addComponentToPane(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y, int width) {
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = width;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(component, gbc);
        }

        protected static void initComponents(JPanel panel, GridBagConstraints gbc, String[] labels, JComponent[] fields) {
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel(labels[i]);
                addComponentToPane(panel, label, gbc, 0, i, 1);
                addComponentToPane(panel, fields[i], gbc, 1, i, 2);
            }
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

        protected JButton createButton(String text, ActionListener actionListener) {
            JButton button = new JButton(text);
            button.addActionListener(actionListener);
            return button;
        }
    }
}
