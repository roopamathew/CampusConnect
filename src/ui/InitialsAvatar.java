package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * Renders an elegant initials avatar circle.
 */
public class InitialsAvatar extends JComponent {

    private String initials = "CC";
    private int size = 44;
    private Color background = AppTheme.INDIGO;
    private Color foreground = Color.WHITE;

    public InitialsAvatar(String name, int size) {
        this(name, size, AppTheme.INDIGO, Color.WHITE);
    }

    public InitialsAvatar(String name, int size, Color bg, Color fg) {
        this.size = size;
        this.background = bg;
        this.foreground = fg;
        setInitialsFromName(name);
        Dimension dim = new Dimension(size, size);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setOpaque(false);
    }

    public void setInitialsFromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.initials = "CC";
            return;
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            this.initials = parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        } else {
            String first = parts[0].substring(0, 1).toUpperCase();
            String last = parts[parts.length - 1].substring(0, 1).toUpperCase();
            this.initials = first + last;
        }
        repaint();
    }

    public void setBackground(Color bg) {
        this.background = bg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(background);
        g2.fillOval(0, 0, size, size);

        // Subtle ring
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawOval(1, 1, size - 3, size - 3);

        g2.setColor(foreground);
        int fontSize = Math.max(11, (int) (size * 0.40f));
        g2.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(initials, x, y);

        g2.dispose();
    }
}
