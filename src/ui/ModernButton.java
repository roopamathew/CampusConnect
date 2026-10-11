package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Modern Swing button with smooth hover effects and antialiased rounded corners.
 * Adapts dynamically to CampusConnect Dark and Light design system.
 */
public class ModernButton extends JButton {

    public enum Variant {
        PRIMARY, SECONDARY, ACCENT, DANGER, SUCCESS, OUTLINE, GHOST
    }

    private Variant variant = Variant.PRIMARY;
    private int cornerRadius = 8;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text) {
        this(text, Variant.PRIMARY);
    }

    public ModernButton(String text, Variant variant) {
        super(text);
        this.variant = variant;
        setFont(AppTheme.BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color bg;
        Color fg;
        Color border = null;

        if (!isEnabled()) {
            bg = AppTheme.isDarkMode ? new Color(30, 41, 59) : new Color(226, 232, 240);
            fg = AppTheme.MUTED;
        } else {
            switch (variant) {
                case PRIMARY:
                    if (isPressed) bg = AppTheme.PRIMARY_DARK.darker();
                    else if (isHovered) bg = AppTheme.PRIMARY_DARK;
                    else bg = AppTheme.PRIMARY;
                    fg = Color.WHITE;
                    break;
                case SECONDARY:
                    if (isPressed) bg = AppTheme.isDarkMode ? AppTheme.CARD_ELEVATED.darker() : AppTheme.LAVENDER_SOFT;
                    else if (isHovered) bg = AppTheme.isDarkMode ? AppTheme.CARD_ELEVATED : new Color(241, 245, 249);
                    else bg = AppTheme.CARD;
                    fg = AppTheme.TEXT;
                    border = isHovered ? AppTheme.PRIMARY : AppTheme.BORDER;
                    break;
                case ACCENT:
                    if (isPressed) bg = AppTheme.ACCENT.darker();
                    else if (isHovered) bg = AppTheme.ACCENT.brighter();
                    else bg = AppTheme.ACCENT;
                    fg = AppTheme.isDarkMode ? Color.BLACK : Color.WHITE;
                    break;
                case DANGER:
                    if (isPressed) bg = AppTheme.DANGER.darker();
                    else if (isHovered) bg = AppTheme.DANGER.brighter();
                    else bg = AppTheme.DANGER;
                    fg = Color.WHITE;
                    break;
                case SUCCESS:
                    if (isPressed) bg = AppTheme.SUCCESS.darker();
                    else if (isHovered) bg = AppTheme.SUCCESS.brighter();
                    else bg = AppTheme.SUCCESS;
                    fg = Color.WHITE;
                    break;
                case OUTLINE:
                    bg = isHovered ? new Color(AppTheme.PRIMARY.getRed(), AppTheme.PRIMARY.getGreen(), AppTheme.PRIMARY.getBlue(), 30) : new Color(0, 0, 0, 0);
                    fg = AppTheme.PRIMARY;
                    border = AppTheme.PRIMARY;
                    break;
                case GHOST:
                default:
                    bg = isHovered ? new Color(AppTheme.TEXT.getRed(), AppTheme.TEXT.getGreen(), AppTheme.TEXT.getBlue(), 20) : new Color(0, 0, 0, 0);
                    fg = AppTheme.TEXT;
                    break;
            }
        }

        // Draw background
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Draw border if needed
        if (border != null) {
            g2.setColor(border);
            g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();

        setForeground(fg);
        super.paintComponent(g);
    }
}
