package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Helpful empty state panel with icon/symbol, headline, description, and optional action.
 */
public class EmptyState extends RoundedCard {

    public EmptyState(String symbol, String headline, String description, String actionText, Runnable onAction) {
        super(16, AppTheme.CARD, AppTheme.BORDER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(16));

        if (symbol != null && !symbol.isEmpty()) {
            JLabel symLabel = new JLabel(symbol);
            symLabel.setFont(AppTheme.BRAND.deriveFont(28f));
            symLabel.setForeground(AppTheme.ACCENT);
            symLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(symLabel);
            add(Box.createVerticalStrut(8));
        }

        JLabel headLabel = new JLabel(headline);
        headLabel.setFont(AppTheme.HEADING);
        headLabel.setForeground(AppTheme.NAVY);
        headLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(headLabel);
        add(Box.createVerticalStrut(6));

        JLabel descLabel = new JLabel("<html><div style='text-align:center;width:380px;'>" + description + "</div></html>");
        descLabel.setFont(AppTheme.BODY);
        descLabel.setForeground(AppTheme.MUTED);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(descLabel);

        if (actionText != null && onAction != null) {
            add(Box.createVerticalStrut(14));
            ModernButton actionBtn = new ModernButton(actionText, ModernButton.Variant.PRIMARY);
            actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionBtn.addActionListener(e -> onAction.run());
            add(actionBtn);
        }

        add(Box.createVerticalStrut(16));
    }
}
