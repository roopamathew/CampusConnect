package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * Shared colours, fonts and button helpers so every screen looks consistent.
 */
public class AppTheme {

    public static final Color NAVY = new Color(26, 35, 58);
    public static final Color NAVY_DARK = new Color(18, 24, 41);
    public static final Color INDIGO = new Color(76, 89, 140);
    public static final Color INDIGO_DARK = new Color(58, 70, 118);
    public static final Color LAVENDER = new Color(196, 186, 216);
    public static final Color LAVENDER_SOFT = new Color(236, 230, 245);
    /** Alias: primary accent used by existing screens. */
    public static final Color TEAL = INDIGO;
    public static final Color TEAL_DARK = INDIGO_DARK;
    /** Quiet champagne gold. */
    public static final Color ACCENT = new Color(196, 168, 122);
    public static final Color BG = new Color(247, 243, 235);
    public static final Color CARD = new Color(252, 250, 245);
    public static final Color TEXT = new Color(26, 35, 58);
    public static final Color MUTED = new Color(110, 104, 122);
    public static final Color BORDER = new Color(226, 218, 206);
    public static final Color SHADOW = new Color(210, 200, 188);
    public static final Color DANGER = new Color(153, 80, 80);
    public static final Color SUCCESS = new Color(74, 122, 96);
    public static final Color NAV_ACTIVE = new Color(58, 70, 118);

    public static final boolean REDUCED_MOTION = false;

    public static final Font BRAND = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SHADOW),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)));
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
        JButton button = baseButton(text, INDIGO, CARD);
        addHover(button, INDIGO, INDIGO_DARK);
        return button;
    }

    public static JButton accentButton(String text) {
        JButton button = baseButton(text, ACCENT, NAVY);
        addHover(button, ACCENT, new Color(176, 148, 102));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = baseButton(text, CARD, TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        addHover(button, CARD, LAVENDER_SOFT);
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = baseButton(text, DANGER, CARD);
        addHover(button, DANGER, new Color(130, 64, 64));
        return button;
    }

    public static JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BOLD);
        button.setForeground(new Color(236, 230, 245));
        button.setBackground(NAVY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.putClientProperty("cc.navActive", Boolean.FALSE);
        addNavHover(button);
        return button;
    }

    public static void setNavActive(JButton button, boolean active) {
        button.putClientProperty("cc.navActive", Boolean.valueOf(active));
        if (active) {
            button.setBackground(NAV_ACTIVE);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, LAVENDER),
                    BorderFactory.createEmptyBorder(12, 17, 12, 20)));
        } else {
            button.setBackground(NAVY);
            button.setForeground(new Color(236, 230, 245));
            button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        }
    }

    public static void styleField(JTextField field) {
        field.setFont(BODY);
        field.setForeground(TEXT);
        field.setBackground(CARD);
        field.setCaretColor(NAVY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static void styleComboBox(javax.swing.JComboBox<?> box) {
        box.setFont(BODY);
        box.setForeground(TEXT);
        box.setBackground(CARD);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    public static void styleTextArea(javax.swing.JTextArea area) {
        area.setFont(BODY);
        area.setForeground(TEXT);
        area.setBackground(CARD);
        area.setCaretColor(NAVY);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(BODY);
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setGridColor(BORDER);
        table.setSelectionBackground(LAVENDER_SOFT);
        table.setSelectionForeground(NAVY);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(BOLD);
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setBackground(LAVENDER_SOFT);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
    }

    private static JButton baseButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BOLD);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private static void addHover(final JButton button, final Color normal, final Color hover) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    blendBackground(button, hover);
                }
            }

            public void mouseExited(MouseEvent e) {
                blendBackground(button, normal);
            }
        });
    }

    private static void addNavHover(final JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (Boolean.TRUE.equals(button.getClientProperty("cc.navActive"))) {
                    return;
                }
                blendBackground(button, INDIGO);
            }

            public void mouseExited(MouseEvent e) {
                if (Boolean.TRUE.equals(button.getClientProperty("cc.navActive"))) {
                    return;
                }
                blendBackground(button, NAVY);
            }
        });
    }

    private static void blendBackground(final JButton button, final Color target) {
        stopBlend(button);
        if (REDUCED_MOTION || GraphicsEnvironment.isHeadless()) {
            button.setBackground(target);
            return;
        }
        final Color start = button.getBackground();
        final Timer timer = new Timer(18, null);
        button.putClientProperty("cc.blend", timer);
        timer.addActionListener(new ActionListener() {
            int step = 0;
            final int steps = 10;

            public void actionPerformed(ActionEvent e) {
                step++;
                float t = step / (float) steps;
                if (t >= 1f) {
                    button.setBackground(target);
                    timer.stop();
                    return;
                }
                button.setBackground(lerp(start, target, t));
            }
        });
        timer.start();
    }

    private static void stopBlend(JButton button) {
        Object existing = button.getClientProperty("cc.blend");
        if (existing instanceof Timer) {
            ((Timer) existing).stop();
        }
    }

    private static Color lerp(Color a, Color b, float t) {
        int r = a.getRed() + Math.round((b.getRed() - a.getRed()) * t);
        int g = a.getGreen() + Math.round((b.getGreen() - a.getGreen()) * t);
        int bch = a.getBlue() + Math.round((b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bch);
    }

    public static void styleCard(Component component) {
        if (component instanceof JComponent) {
            JComponent panel = (JComponent) component;
            panel.setBackground(CARD);
            panel.setBorder(cardBorder());
            panel.setOpaque(true);
        }
    }
}
