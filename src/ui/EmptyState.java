package ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * Helpful empty state panel with icon/symbol, headline, description, and optional action.
 */
public class EmptyState extends RoundedCard {

    public EmptyState(String symbol, String headline, String description, String actionText, Runnable onAction) {
        super(16, null, null);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(16));

        if (symbol != null && !symbol.isEmpty()) {
            JLabel symLabel = new JLabel(symbol);
            symLabel.setFont(AppTheme.BRAND.deriveFont(32f));
            symLabel.setForeground(AppTheme.ACCENT);
            symLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(symLabel);
            add(Box.createVerticalStrut(8));
        }

        JLabel headLabel = new JLabel(headline);
        headLabel.setFont(AppTheme.HEADING.deriveFont(18f));
        headLabel.setForeground(AppTheme.TEXT);
        headLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(headLabel);
        add(Box.createVerticalStrut(6));

        JLabel descLabel = new JLabel("<html><div style='text-align:center;width:400px;'>" + description + "</div></html>");
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
