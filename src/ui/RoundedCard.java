package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Polished card container with smooth antialiased rounded corners and subtle border.
 */
public class RoundedCard extends JPanel {

    private int cornerRadius = 14;
    private Color fillColor = AppTheme.CARD;
    private Color borderColor = AppTheme.BORDER;
    private boolean hoverable = false;
    private boolean isHovered = false;

    public RoundedCard() {
        this(14, AppTheme.CARD, AppTheme.BORDER);
    }

    public RoundedCard(LayoutManager layout) {
        super(layout);
        init(14, AppTheme.CARD, AppTheme.BORDER);
    }

    public RoundedCard(int cornerRadius) {
        this(cornerRadius, AppTheme.CARD, AppTheme.BORDER);
    }

    public RoundedCard(int cornerRadius, Color fill, Color border) {
        init(cornerRadius, fill, border);
    }

    private void init(int cornerRadius, Color fill, Color border) {
        this.cornerRadius = cornerRadius;
        this.fillColor = fill;
        this.borderColor = border;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
        if (hoverable) {
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }
    }

    public void setFillColor(Color fill) {
        this.fillColor = fill;
        repaint();
    }

    public void setBorderColor(Color border) {
        this.borderColor = border;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth() - 1;
        int h = getHeight() - 1;

        // Subtle shadow / outer stroke on hover
        if (hoverable && isHovered) {
            g2.setColor(new Color(AppTheme.INDIGO.getRed(), AppTheme.INDIGO.getGreen(), AppTheme.INDIGO.getBlue(), 24));
            g2.fillRoundRect(0, 0, w, h, cornerRadius + 2, cornerRadius + 2);
        }

        // Fill background
        g2.setColor(fillColor);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Border
        Color stroke = (hoverable && isHovered) ? AppTheme.INDIGO : borderColor;
        g2.setColor(stroke);
        g2.drawRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
