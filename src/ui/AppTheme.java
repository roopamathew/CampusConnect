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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * Global Design System for CampusConnect.
 * Defaults to the approved Midnight/Charcoal Dark Theme, with dynamic support for Light Theme.
 */
public class AppTheme {

    // --- State & Listeners ---
    public static boolean isDarkMode = true;
    private static final List<Runnable> themeListeners = new CopyOnWriteArrayList<Runnable>();

    // --- Dark Palette (Source of Truth) ---
    public static final Color DARK_BG = new Color(11, 15, 23);             // #0B0F17 Deep midnight charcoal
    public static final Color DARK_NAVY = new Color(18, 24, 38);           // #121826 Elevated navy
    public static final Color DARK_NAVY_DARK = new Color(13, 17, 28);      // #0D111C Darkest navy
    public static final Color DARK_CARD = new Color(22, 30, 46);           // #161E2E Modern navy card
    public static final Color DARK_CARD_ELEVATED = new Color(28, 38, 58);  // #1C263A Elevated surface
    public static final Color DARK_TEXT = new Color(241, 245, 249);        // #F1F5F9 Crisp white
    public static final Color DARK_MUTED = new Color(148, 163, 184);       // #94A3B8 Readable muted grey
    public static final Color DARK_BORDER = new Color(38, 50, 74);         // #26324A Refined border
    public static final Color DARK_SHADOW = new Color(7, 10, 16);          // Deep shadow
    public static final Color DARK_PRIMARY = new Color(37, 99, 235);       // #2563EB Electric Blue
    public static final Color DARK_PRIMARY_DARK = new Color(29, 78, 216);  // #1D4ED8 Deep Blue
    public static final Color DARK_ACCENT = new Color(245, 158, 11);       // #F59E0B Warm Amber Gold
    public static final Color DARK_SUCCESS = new Color(16, 185, 129);      // #10B981 Emerald
    public static final Color DARK_DANGER = new Color(239, 68, 68);        // #EF4444 Crimson
    public static final Color DARK_LAVENDER = new Color(165, 180, 252);    // #A5B4FC Lavender highlight
    public static final Color DARK_LAVENDER_SOFT = new Color(30, 41, 59);  // #1E293B Subtle fill

    // --- Light Palette ---
    public static final Color LIGHT_BG = new Color(248, 249, 250);
    public static final Color LIGHT_NAVY = new Color(24, 32, 52);
    public static final Color LIGHT_NAVY_DARK = new Color(15, 22, 38);
    public static final Color LIGHT_CARD = new Color(255, 255, 255);
    public static final Color LIGHT_CARD_ELEVATED = new Color(241, 245, 249);
    public static final Color LIGHT_TEXT = new Color(15, 23, 42);
    public static final Color LIGHT_MUTED = new Color(100, 116, 139);
    public static final Color LIGHT_BORDER = new Color(226, 232, 240);
    public static final Color LIGHT_SHADOW = new Color(203, 213, 225);
    public static final Color LIGHT_PRIMARY = new Color(37, 99, 235);
    public static final Color LIGHT_PRIMARY_DARK = new Color(29, 78, 216);
    public static final Color LIGHT_ACCENT = new Color(217, 119, 6);
    public static final Color LIGHT_SUCCESS = new Color(16, 185, 129);
    public static final Color LIGHT_DANGER = new Color(220, 38, 38);
    public static final Color LIGHT_LAVENDER = new Color(99, 102, 241);
    public static final Color LIGHT_LAVENDER_SOFT = new Color(238, 242, 255);

    // --- Active Mutable Theme Colors ---
    public static Color BG = DARK_BG;
    public static Color NAVY = DARK_NAVY;
    public static Color NAVY_DARK = DARK_NAVY_DARK;
    public static Color CARD = DARK_CARD;
    public static Color CARD_ELEVATED = DARK_CARD_ELEVATED;
    public static Color TEXT = DARK_TEXT;
    public static Color MUTED = DARK_MUTED;
    public static Color BORDER = DARK_BORDER;
    public static Color SHADOW = DARK_SHADOW;
    public static Color INDIGO = DARK_PRIMARY;
    public static Color INDIGO_DARK = DARK_PRIMARY_DARK;
    public static Color PRIMARY = DARK_PRIMARY;
    public static Color PRIMARY_DARK = DARK_PRIMARY_DARK;
    public static Color TEAL = DARK_PRIMARY;
    public static Color TEAL_DARK = DARK_PRIMARY_DARK;
    public static Color ACCENT = DARK_ACCENT;
    public static Color SUCCESS = DARK_SUCCESS;
    public static Color DANGER = DARK_DANGER;
    public static Color LAVENDER = DARK_LAVENDER;
    public static Color LAVENDER_SOFT = DARK_LAVENDER_SOFT;
    public static Color NAV_ACTIVE = DARK_PRIMARY;

    public static final boolean REDUCED_MOTION = false;

    // --- Typography Hierarchy ---
    public static final Font BRAND = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOLD = new Font("Segoe UI", Font.BOLD, 13);

    static {
        updateColors();
    }

    public static void updateColors() {
        if (isDarkMode) {
            BG = DARK_BG;
            NAVY = DARK_NAVY;
            NAVY_DARK = DARK_NAVY_DARK;
            CARD = DARK_CARD;
            CARD_ELEVATED = DARK_CARD_ELEVATED;
            TEXT = DARK_TEXT;
            MUTED = DARK_MUTED;
            BORDER = DARK_BORDER;
            SHADOW = DARK_SHADOW;
            INDIGO = DARK_PRIMARY;
            INDIGO_DARK = DARK_PRIMARY_DARK;
            PRIMARY = DARK_PRIMARY;
            PRIMARY_DARK = DARK_PRIMARY_DARK;
            TEAL = DARK_PRIMARY;
            TEAL_DARK = DARK_PRIMARY_DARK;
            ACCENT = DARK_ACCENT;
            SUCCESS = DARK_SUCCESS;
            DANGER = DARK_DANGER;
            LAVENDER = DARK_LAVENDER;
            LAVENDER_SOFT = DARK_LAVENDER_SOFT;
            NAV_ACTIVE = DARK_PRIMARY;
        } else {
            BG = LIGHT_BG;
            NAVY = LIGHT_NAVY;
            NAVY_DARK = LIGHT_NAVY_DARK;
            CARD = LIGHT_CARD;
            CARD_ELEVATED = LIGHT_CARD_ELEVATED;
            TEXT = LIGHT_TEXT;
            MUTED = LIGHT_MUTED;
            BORDER = LIGHT_BORDER;
            SHADOW = LIGHT_SHADOW;
            INDIGO = LIGHT_PRIMARY;
            INDIGO_DARK = LIGHT_PRIMARY_DARK;
            PRIMARY = LIGHT_PRIMARY;
            PRIMARY_DARK = LIGHT_PRIMARY_DARK;
            TEAL = LIGHT_PRIMARY;
            TEAL_DARK = LIGHT_PRIMARY_DARK;
            ACCENT = LIGHT_ACCENT;
            SUCCESS = LIGHT_SUCCESS;
            DANGER = LIGHT_DANGER;
            LAVENDER = LIGHT_LAVENDER;
            LAVENDER_SOFT = LIGHT_LAVENDER_SOFT;
            NAV_ACTIVE = LIGHT_PRIMARY;
        }
    }

    public static void setDarkMode(boolean dark) {
        if (isDarkMode != dark) {
            isDarkMode = dark;
            updateColors();
            for (Runnable listener : themeListeners) {
                try {
                    listener.run();
                } catch (Exception e) {
                    // Suppress listener exceptions during dynamic repaint
                }
            }
        }
    }

    public static void toggleTheme() {
        setDarkMode(!isDarkMode);
    }

    public static void addThemeListener(Runnable listener) {
        if (listener != null && !themeListeners.contains(listener)) {
            themeListeners.add(listener);
        }
    }

    public static void removeThemeListener(Runnable listener) {
        themeListeners.remove(listener);
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18));
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
        JButton button = baseButton(text, PRIMARY, Color.WHITE);
        addHover(button, PRIMARY, PRIMARY_DARK);
        return button;
    }

    public static JButton accentButton(String text) {
        JButton button = baseButton(text, ACCENT, isDarkMode ? Color.BLACK : Color.WHITE);
        addHover(button, ACCENT, ACCENT.darker());
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = baseButton(text, CARD, TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        addHover(button, CARD, isDarkMode ? CARD_ELEVATED : LAVENDER_SOFT);
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = baseButton(text, DANGER, Color.WHITE);
        addHover(button, DANGER, DANGER.darker());
        return button;
    }

    public static JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BOLD);
        button.setForeground(new Color(226, 232, 240));
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
                    BorderFactory.createMatteBorder(0, 3, 0, 0, isDarkMode ? Color.WHITE : ACCENT),
                    BorderFactory.createEmptyBorder(12, 17, 12, 20)));
        } else {
            button.setBackground(NAVY);
            button.setForeground(new Color(203, 213, 225));
            button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        }
    }

    public static void styleField(JTextField field) {
        field.setFont(BODY);
        field.setForeground(TEXT);
        field.setBackground(isDarkMode ? new Color(18, 24, 38) : Color.WHITE);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static void styleComboBox(javax.swing.JComboBox<?> box) {
        box.setFont(BODY);
        box.setForeground(TEXT);
        box.setBackground(isDarkMode ? new Color(18, 24, 38) : Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    public static void styleTextArea(javax.swing.JTextArea area) {
        area.setFont(BODY);
        area.setForeground(TEXT);
        area.setBackground(isDarkMode ? new Color(18, 24, 38) : Color.WHITE);
        area.setCaretColor(TEXT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(BODY);
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setGridColor(BORDER);
        table.setSelectionBackground(isDarkMode ? CARD_ELEVATED : LAVENDER_SOFT);
        table.setSelectionForeground(isDarkMode ? Color.WHITE : NAVY);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(BOLD);
        table.getTableHeader().setForeground(isDarkMode ? Color.WHITE : NAVY);
        table.getTableHeader().setBackground(isDarkMode ? NAVY : LAVENDER_SOFT);
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
                blendBackground(button, isDarkMode ? CARD_ELEVATED : INDIGO);
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
            final int steps = 8;

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
