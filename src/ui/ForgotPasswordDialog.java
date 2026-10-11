package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Honest password recovery dialog.
 * Clearly documents that SMTP email dispatch is not configured in this offline prototype,
 * verifies user existence, and permits simulated password reset.
 */
public class ForgotPasswordDialog extends JDialog {

    private final JTextField identifierField = new JTextField();
    private final JPasswordField newPasswordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JPanel resetSection = new JPanel(new GridBagLayout());
    private User matchedUser = null;

    public ForgotPasswordDialog(JFrame parent) {
        super(parent, "Account Recovery – CampusConnect", true);
        setSize(480, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel("Password Recovery");
        title.setFont(AppTheme.TITLE);
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Verify your registered identity to reset your credentials");
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
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(AppTheme.CARD);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.weightx = 1;

        // Configuration notice
        RoundedCard noticeCard = new RoundedCard(10, AppTheme.LAVENDER_SOFT, AppTheme.LAVENDER);
        noticeCard.setLayout(new BorderLayout());
        JLabel noticeText = new JLabel("<html><b>Environment Notice:</b> Automated SMTP email dispatch is not configured in this offline in-memory desktop build. Account verification is performed directly against in-memory records.</html>");
        noticeText.setFont(AppTheme.SMALL);
        noticeText.setForeground(AppTheme.NAVY);
        noticeCard.add(noticeText, BorderLayout.CENTER);

        int row = 0;
        gbc.gridy = row++;
        body.add(noticeCard, gbc);

        gbc.gridy = row++;
        JLabel idLabel = new JLabel("Username or Registered Email");
        idLabel.setFont(AppTheme.BOLD);
        idLabel.setForeground(AppTheme.NAVY);
        body.add(idLabel, gbc);

        AppTheme.styleField(identifierField);
        gbc.gridy = row++;
        body.add(identifierField, gbc);

        ModernButton verifyBtn = new ModernButton("Verify Account", ModernButton.Variant.SECONDARY);
        gbc.gridy = row++;
        body.add(verifyBtn, gbc);

        // Reset section (hidden until verified)
        resetSection.setOpaque(false);
        resetSection.setVisible(false);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.gridx = 0;
        gbcR.fill = GridBagConstraints.HORIZONTAL;
        gbR_setup(resetSection, gbcR);

        gbc.gridy = row++;
        body.add(resetSection, gbc);

        root.add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(AppTheme.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton closeBtn = new ModernButton("Close", ModernButton.Variant.SECONDARY);
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        ModernButton saveBtn = new ModernButton("Update Password", ModernButton.Variant.PRIMARY);
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(e -> attemptReset());
        footer.add(saveBtn);

        verifyBtn.addActionListener(e -> {
            String id = identifierField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username or registered email address.",
                        "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User found = null;
            for (User u : DummyData.users) {
                if (u.getUsername().equalsIgnoreCase(id) || u.getEmail().equalsIgnoreCase(id)) {
                    found = u;
                    break;
                }
            }
            if (found == null) {
                JOptionPane.showMessageDialog(this, "No registered account matches '" + id + "'.",
                        "Account Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            matchedUser = found;
            resetSection.setVisible(true);
            saveBtn.setEnabled(true);
            verifyBtn.setEnabled(false);
            identifierField.setEditable(false);
            JOptionPane.showMessageDialog(this,
                    "<html>Account verified for: <b>" + matchedUser.getFullName() + "</b> (" + matchedUser.getRole() + ")<br>"
                            + "College: " + matchedUser.getCollege() + "<br><br>"
                            + "You may now enter a new password below.</html>",
                    "Identity Verified", JOptionPane.INFORMATION_MESSAGE);
            revalidate();
            repaint();
        });

        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void gbR_setup(JPanel panel, GridBagConstraints gbcR) {
        gbcR.insets = new Insets(4, 0, 4, 0);
        gbcR.weightx = 1;

        int r = 0;
        gbcR.gridy = r++;
        JLabel npLabel = new JLabel("New Password (min. 4 characters)");
        npLabel.setFont(AppTheme.BOLD);
        npLabel.setForeground(AppTheme.NAVY);
        panel.add(npLabel, gbcR);

        AppTheme.styleField(newPasswordField);
        gbcR.gridy = r++;
        panel.add(newPasswordField, gbcR);

        gbcR.gridy = r++;
        JLabel cpLabel = new JLabel("Confirm New Password");
        cpLabel.setFont(AppTheme.BOLD);
        cpLabel.setForeground(AppTheme.NAVY);
        panel.add(cpLabel, gbcR);

        AppTheme.styleField(confirmPasswordField);
        gbcR.gridy = r++;
        panel.add(confirmPasswordField, gbcR);
    }

    private void attemptReset() {
        if (matchedUser == null) return;
        String p1 = new String(newPasswordField.getPassword());
        String p2 = new String(confirmPasswordField.getPassword());

        if (p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter and confirm your new password.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (p1.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.",
                    "Password Too Short", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Mismatch", JOptionPane.ERROR_MESSAGE);
            return;
        }

        matchedUser.setPassword(p1);
        JOptionPane.showMessageDialog(this,
                "<html><b>Password Updated Successfully!</b><br><br>"
                        + "Your password has been changed in-memory for user: <b>" + matchedUser.getUsername() + "</b>.<br>"
                        + "You can now log in using your new credentials.</html>",
                "Password Reset", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
