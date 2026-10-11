package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Honest Google Sign-In boundary dialog.
 * Clearly documents the external OAuth 2.0 setup required and never silently logs in.
 */
public class GoogleAuthDialog extends JDialog {

    public GoogleAuthDialog(JFrame parent) {
        super(parent, "Google Sign-In – Configuration Boundary", true);
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel title = new JLabel("Google OAuth 2.0 Authentication");
        title.setFont(AppTheme.TITLE);
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Enterprise & Institutional SSO Integration");
        sub.setFont(AppTheme.SMALL);
        sub.setForeground(AppTheme.LAVENDER);
        JPanel topBox = new JPanel();
        topBox.setOpaque(false);
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.add(title);
        topBox.add(Box.createVerticalStrut(4));
        topBox.add(sub);
        header.add(topBox, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Content
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(AppTheme.CARD);
        body.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JLabel info = new JLabel("<html><div style='width:440px;line-height:1.5;'>"
                + "<b style='color:#1A233A;font-size:14px;'>OAuth Client Setup Required</b><br><br>"
                + "Institutional Google Sign-In requires external OAuth 2.0 client credentials from the "
                + "Google Cloud Console. In this offline desktop prototype, external web services and redirect listeners "
                + "are disabled.<br><br>"
                + "<b>Required configuration to enable in production:</b><br>"
                + "• Register application in <i>Google Cloud Platform</i><br>"
                + "• Configure OAuth Consent Screen & authorized redirect URI<br>"
                + "• Download <code>client_secrets.json</code> and place in application classpath<br>"
                + "• Configure the Google API Client Library for Java (Desktop Flow)<br><br>"
                + "<i style='color:#6E687A;'>Please authenticate using username and password credentials.</i>"
                + "</div></html>");
        info.setFont(AppTheme.BODY);
        info.setForeground(AppTheme.TEXT);
        body.add(info);

        root.add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(AppTheme.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton okBtn = new ModernButton("Understood", ModernButton.Variant.PRIMARY);
        okBtn.addActionListener(e -> dispose());
        footer.add(okBtn);

        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }
}
