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
 */
public class PillBadge extends JLabel {

    private Color bgColor;
    private Color textColor;
    private Color borderColor;

    public PillBadge(String text) {
        this(text, AppTheme.LAVENDER_SOFT, AppTheme.INDIGO, AppTheme.LAVENDER);
    }

    public PillBadge(String text, Color bg, Color textCol) {
        this(text, bg, textCol, null);
    }

    public PillBadge(String text, Color bg, Color textCol, Color borderCol) {
        super(text);
        this.bgColor = bg;
        this.textColor = textCol;
        this.borderColor = borderCol;
        setFont(AppTheme.SMALL.deriveFont(Font.BOLD, 11.5f));
        setForeground(textColor);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
    }

    public static PillBadge category(String category) {
        Color bg = AppTheme.LAVENDER_SOFT;
        Color fg = AppTheme.INDIGO;
        Color border = new Color(210, 202, 228);
        if ("Hackathon".equalsIgnoreCase(category) || "Technical".equalsIgnoreCase(category)) {
            bg = new Color(230, 238, 252);
            fg = new Color(42, 78, 140);
            border = new Color(195, 215, 245);
        } else if ("Cultural".equalsIgnoreCase(category) || "Arts".equalsIgnoreCase(category)) {
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
        }
        return new PillBadge(category, bg, fg, border);
    }

    public static PillBadge status(String status) {
        Color bg;
        Color fg;
        Color border;
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
        return new PillBadge(status, bg, fg, border);
    }

    public static PillBadge role(String role) {
        if ("SUPER_ADMIN".equalsIgnoreCase(role)) {
            return new PillBadge("SUPER ADMIN", new Color(254, 244, 230), new Color(165, 105, 20), new Color(235, 205, 155));
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            return new PillBadge("COLLEGE ADMIN", new Color(235, 240, 252), new Color(45, 80, 145), new Color(195, 215, 245));
        } else {
            return new PillBadge("STUDENT", new Color(240, 248, 242), AppTheme.SUCCESS, new Color(190, 230, 205));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int r = h;

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
