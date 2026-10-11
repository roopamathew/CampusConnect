package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Clean pill-shaped badge for categories, event status, colleges, and roles.
 * Supports refined dark-theme contrast and light-theme highlights.
 */
public class PillBadge extends JLabel {

    private Color bgColor;
    private Color textColor;
    private Color borderColor;

    public PillBadge(String text) {
        this(text, AppTheme.isDarkMode ? new Color(30, 41, 59) : AppTheme.LAVENDER_SOFT,
                AppTheme.isDarkMode ? AppTheme.TEXT : AppTheme.INDIGO,
                AppTheme.isDarkMode ? AppTheme.BORDER : AppTheme.LAVENDER);
    }

    public PillBadge(String text, Color bg, Color textCol) {
        this(text, bg, textCol, null);
    }

    public PillBadge(String text, Color bg, Color textCol, Color borderCol) {
        super(text);
        this.bgColor = bg;
        this.textColor = textCol;
        this.borderColor = borderCol;
        setFont(AppTheme.SMALL.deriveFont(Font.BOLD, 11f));
        setForeground(textColor);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
    }

    public static PillBadge category(String category) {
        Color bg;
        Color fg;
        Color border;

        if (AppTheme.isDarkMode) {
            if ("Hackathon".equalsIgnoreCase(category) || "Technical".equalsIgnoreCase(category) || "Technology".equalsIgnoreCase(category)) {
                bg = new Color(23, 37, 84);
                fg = new Color(96, 165, 250);
                border = new Color(30, 58, 138);
            } else if ("Cultural".equalsIgnoreCase(category) || "Arts".equalsIgnoreCase(category) || "Music".equalsIgnoreCase(category)) {
                bg = new Color(76, 29, 149);
                fg = new Color(244, 114, 182);
                border = new Color(109, 40, 217);
            } else if ("Competition".equalsIgnoreCase(category) || "Sports".equalsIgnoreCase(category)) {
                bg = new Color(6, 78, 59);
                fg = new Color(52, 211, 153);
                border = new Color(5, 150, 105);
            } else if ("Workshop".equalsIgnoreCase(category) || "Seminar".equalsIgnoreCase(category)) {
                bg = new Color(69, 26, 3);
                fg = new Color(251, 191, 36);
                border = new Color(146, 64, 14);
            } else {
                bg = new Color(30, 41, 59);
                fg = new Color(203, 213, 225);
                border = new Color(51, 65, 85);
            }
        } else {
            if ("Hackathon".equalsIgnoreCase(category) || "Technical".equalsIgnoreCase(category) || "Technology".equalsIgnoreCase(category)) {
                bg = new Color(230, 238, 252);
                fg = new Color(42, 78, 140);
                border = new Color(195, 215, 245);
            } else if ("Cultural".equalsIgnoreCase(category) || "Arts".equalsIgnoreCase(category) || "Music".equalsIgnoreCase(category)) {
                bg = new Color(252, 240, 246);
                fg = new Color(145, 45, 95);
                border = new Color(245, 205, 225);
            } else if ("Competition".equalsIgnoreCase(category) || "Sports".equalsIgnoreCase(category)) {
                bg = new Color(240, 248, 240);
                fg = new Color(42, 118, 65);
                border = new Color(195, 230, 205);
            } else if ("Workshop".equalsIgnoreCase(category) || "Seminar".equalsIgnoreCase(category)) {
                bg = new Color(254, 246, 234);
                fg = new Color(150, 100, 30);
                border = new Color(240, 218, 185);
            } else {
                bg = AppTheme.LAVENDER_SOFT;
                fg = AppTheme.NAVY;
                border = AppTheme.LAVENDER;
            }
        }
        return new PillBadge(category, bg, fg, border);
    }

    public static PillBadge status(String status) {
        Color bg;
        Color fg;
        Color border;

        if (AppTheme.isDarkMode) {
            if ("Registration Open".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(status) || "Active".equalsIgnoreCase(status)) {
                bg = new Color(6, 78, 59);
                fg = new Color(52, 211, 153);
                border = new Color(5, 150, 105);
            } else if ("Upcoming".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
                bg = new Color(69, 26, 3);
                fg = new Color(251, 191, 36);
                border = new Color(146, 64, 14);
            } else if ("Completed".equalsIgnoreCase(status) || "Registration Closed".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status)) {
                bg = new Color(69, 10, 10);
                fg = new Color(248, 113, 113);
                border = new Color(153, 27, 27);
            } else {
                bg = new Color(30, 41, 59);
                fg = new Color(203, 213, 225);
                border = new Color(51, 65, 85);
            }
        } else {
            if ("Registration Open".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(status) || "Active".equalsIgnoreCase(status)) {
                bg = new Color(234, 248, 238);
                fg = AppTheme.SUCCESS;
                border = new Color(185, 230, 200);
            } else if ("Upcoming".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
                bg = new Color(254, 246, 234);
                fg = new Color(160, 105, 20);
                border = new Color(240, 218, 180);
            } else if ("Completed".equalsIgnoreCase(status) || "Registration Closed".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status)) {
                bg = new Color(252, 238, 238);
                fg = AppTheme.DANGER;
                border = new Color(242, 195, 195);
            } else {
                bg = AppTheme.LAVENDER_SOFT;
                fg = AppTheme.NAVY;
                border = AppTheme.LAVENDER;
            }
        }
        return new PillBadge(status, bg, fg, border);
    }

    public static PillBadge role(String role) {
        if ("SUPER_ADMIN".equalsIgnoreCase(role)) {
            return AppTheme.isDarkMode
                    ? new PillBadge("SUPER ADMIN", new Color(69, 26, 3), new Color(251, 191, 36), new Color(146, 64, 14))
                    : new PillBadge("SUPER ADMIN", new Color(254, 244, 230), new Color(165, 105, 20), new Color(235, 205, 155));
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            return AppTheme.isDarkMode
                    ? new PillBadge("COLLEGE ADMIN", new Color(23, 37, 84), new Color(96, 165, 250), new Color(30, 58, 138))
                    : new PillBadge("COLLEGE ADMIN", new Color(235, 240, 252), new Color(45, 80, 145), new Color(195, 215, 245));
        } else {
            return AppTheme.isDarkMode
                    ? new PillBadge("STUDENT", new Color(6, 78, 59), new Color(52, 211, 153), new Color(5, 150, 105))
                    : new PillBadge("STUDENT", new Color(240, 248, 242), AppTheme.SUCCESS, new Color(190, 230, 205));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int r = h - 2;

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, w, h, r, r);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, r, r);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
