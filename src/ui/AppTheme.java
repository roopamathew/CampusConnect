package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Shared colours, fonts and button helpers so every screen looks consistent.
 */
public class AppTheme {

    public static final Color NAVY = new Color(15, 32, 67);
    public static final Color NAVY_DARK = new Color(10, 22, 48);
    public static final Color TEAL = new Color(14, 116, 144);
    public static final Color TEAL_DARK = new Color(8, 90, 112);
    public static final Color ACCENT = new Color(245, 158, 11);
    public static final Color BG = new Color(241, 245, 249);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(30, 41, 59);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color DANGER = new Color(185, 28, 28);
    public static final Color SUCCESS = new Color(22, 163, 74);

    public static final Font BRAND = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    public static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADING);
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel muted(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SMALL);
        label.setForeground(MUTED);
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton button = baseButton(text, TEAL, Color.WHITE);
        addHover(button, TEAL, TEAL_DARK);
        return button;
    }

    public static JButton accentButton(String text) {
        JButton button = baseButton(text, ACCENT, NAVY);
        addHover(button, ACCENT, new Color(217, 119, 6));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = baseButton(text, Color.WHITE, TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = baseButton(text, DANGER, Color.WHITE);
        addHover(button, DANGER, new Color(153, 27, 27));
        return button;
    }

    public static JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(NAVY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        addHover(button, NAVY, TEAL);
        return button;
    }

    private static JButton baseButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BOLD);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private static void addHover(final JButton button, final Color normal, final Color hover) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                }
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

    public static void styleCard(Component component) {
        if (component instanceof javax.swing.JComponent) {
            javax.swing.JComponent panel = (javax.swing.JComponent) component;
            panel.setBackground(CARD);
            panel.setBorder(cardBorder());
            panel.setOpaque(true);
        }
    }
}
