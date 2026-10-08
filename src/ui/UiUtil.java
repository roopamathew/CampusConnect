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
        page.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        JLabel heading = new JLabel(title);
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);
        heading.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        page.add(heading);

        if (subtitle != null) {
            JLabel sub = AppTheme.muted(subtitle);
            sub.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            page.add(Box.createVerticalStrut(4));
            page.add(sub);
        }
        page.add(Box.createVerticalStrut(16));
        return page;
    }

    public static JPanel statCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        AppTheme.styleCard(card);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppTheme.BRAND);
        valueLabel.setForeground(accent);
        JLabel titleLabel = AppTheme.muted(title);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(160, 90));
        return card;
    }

    public static JPanel statsRow(JPanel... cards) {
        JPanel row = new JPanel(new GridLayout(1, cards.length, 12, 12));
        row.setOpaque(false);
        row.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        for (int i = 0; i < cards.length; i++) {
            row.add(cards[i]);
        }
        return row;
    }

    public static void stretch(JComponent component) {
        component.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height));
    }
}
