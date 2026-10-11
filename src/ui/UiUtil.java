package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class UiUtil {

    public static JScrollPane scroll(JComponent content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.BG);
        wrapper.add(content, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(AppTheme.BG);
        return scroll;
    }

    public static JPanel page(String title, String subtitle) {
        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        JLabel heading = new JLabel(title);
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);
        heading.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        page.add(heading);

        if (subtitle != null) {
            JLabel sub = AppTheme.muted(subtitle);
            sub.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            page.add(Box.createVerticalStrut(6));
            page.add(sub);
        }
        page.add(Box.createVerticalStrut(20));
        return page;
    }

    public static JPanel statCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        AppTheme.styleCard(card);
        JLabel titleLabel = AppTheme.muted(title);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppTheme.BRAND);
        valueLabel.setForeground(AppTheme.NAVY);
        titleLabel.setForeground(accent == null ? AppTheme.MUTED : accent);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(170, 96));
        return card;
    }

    public static JPanel statsRow(JPanel... cards) {
        JPanel row = new JPanel(new GridLayout(1, cards.length, 16, 16));
        row.setOpaque(false);
        row.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        for (int i = 0; i < cards.length; i++) {
            row.add(cards[i]);
        }
        return row;
    }

    public static void stretch(JComponent component) {
        component.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height));
    }

    public static JPanel modernStatCard(String title, String value, String subtitle, Color accent) {
        RoundedCard card = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new BorderLayout(0, 6));
        card.setPreferredSize(new Dimension(200, 105));
        card.setMaximumSize(new Dimension(280, 110));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SMALL.deriveFont(java.awt.Font.BOLD));
        titleLabel.setForeground(AppTheme.MUTED);
        top.add(titleLabel, BorderLayout.WEST);

        if (accent != null) {
            JPanel accentDot = new JPanel() {
                protected void paintComponent(java.awt.Graphics g) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(accent);
                    g2.fillOval(0, 2, 8, 8);
                    g2.dispose();
                }
            };
            accentDot.setOpaque(false);
            accentDot.setPreferredSize(new Dimension(10, 12));
            top.add(accentDot, BorderLayout.EAST);
        }

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(AppTheme.BRAND.deriveFont(26f));
        valLabel.setForeground(AppTheme.NAVY);

        JLabel subLabel = new JLabel(subtitle != null ? subtitle : "");
        subLabel.setFont(AppTheme.SMALL.deriveFont(11.5f));
        subLabel.setForeground(accent != null ? accent : AppTheme.MUTED);

        card.add(top, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        card.add(subLabel, BorderLayout.SOUTH);
        return card;
    }

    public static JPanel pageHeader(String title, String subtitle, JComponent actionRight) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.TITLE);
        titleLabel.setForeground(AppTheme.NAVY);
        left.add(titleLabel);

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel subLabel = AppTheme.muted(subtitle);
            left.add(Box.createVerticalStrut(4));
            left.add(subLabel);
        }

        header.add(left, BorderLayout.WEST);
        if (actionRight != null) {
            header.add(actionRight, BorderLayout.EAST);
        }
        return header;
    }

    public static JPanel sectionTitle(String title, String subtitle) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(title);
        heading.setFont(AppTheme.HEADING);
        heading.setForeground(AppTheme.NAVY);
        wrap.add(heading);

        if (subtitle != null && !subtitle.isEmpty()) {
            wrap.add(Box.createVerticalStrut(2));
            wrap.add(AppTheme.muted(subtitle));
        }
        return wrap;
    }
}
