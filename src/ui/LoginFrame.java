package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Completely redesigned, premium login screen for CampusConnect.
 * Features an authentic midnight dark theme split-screen layout:
 * - Left panel: Elegant branding, wordmark, tagline, mission intro, and 3 feature highlights.
 * - Right panel: Clean, centered login card with username/email login, password show/hide,
 *   Forgot password link, prominent Sign In action, and secondary registration/admin links.
 * 
 * Fully responsive, keyboard accessible, and free of prototype demo buttons.
 */
public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton showHideBtn = new JButton("Show");
    private final ModernButton loginBtn = new ModernButton("Sign In", ModernButton.Variant.PRIMARY);

    private boolean passwordVisible = false;
    private boolean isLoggingIn = false;

    public LoginFrame() {
        setTitle("CampusConnect – Discover Beyond Your Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(920, 600));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(11, 15, 23)); // Deep midnight navy background

        root.add(buildLeftBrandingPanel(), BorderLayout.WEST);
        root.add(buildRightLoginPanel(), BorderLayout.CENTER);

        setContentPane(root);

        // Keyboard navigation and actions
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> attemptLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    /**
     * Left branding section (approx 42-45% window width) with custom-painted
     * midnight gradient, layered curve divider, brand wordmark, tagline, and feature highlights.
     */
    private JPanel buildLeftBrandingPanel() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Deep midnight gradient fill
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(15, 23, 42),
                        w, h, new Color(9, 13, 24));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // Layered subtle curved divider on the right edge
                g2.setColor(new Color(37, 99, 235, 16));
                Path2D p1 = new Path2D.Double();
                p1.moveTo(w - 32, 0);
                p1.curveTo(w + 12, h * 0.35, w - 48, h * 0.65, w - 12, h);
                p1.lineTo(w, h);
                p1.lineTo(w, 0);
                p1.closePath();
                g2.fill(p1);

                g2.setColor(new Color(59, 130, 246, 26));
                Path2D p2 = new Path2D.Double();
                p2.moveTo(w - 20, 0);
                p2.curveTo(w + 14, h * 0.40, w - 32, h * 0.70, w - 6, h);
                p2.lineTo(w, h);
                p2.lineTo(w, 0);
                p2.closePath();
                g2.fill(p2);

                // Vertical separator edge
                g2.setColor(new Color(30, 41, 59));
                g2.drawLine(w - 1, 0, w - 1, h);

                g2.dispose();
            }
        };

        hero.setPreferredSize(new Dimension(460, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(44, 42, 40, 42));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // 1. Brand Logo Mark + Name
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(LEFT_ALIGNMENT);

        brandRow.add(new LogoMark());

        JPanel brandTextCol = new JPanel();
        brandTextCol.setOpaque(false);
        brandTextCol.setLayout(new BoxLayout(brandTextCol, BoxLayout.Y_AXIS));

        JLabel brandName = new JLabel("CampusConnect");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandName.setForeground(Color.WHITE);
        brandTextCol.add(brandName);

        JLabel brandTagline = new JLabel("Discover Beyond Your Campus");
        brandTagline.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brandTagline.setForeground(new Color(56, 189, 248)); // Vibrant Sky Blue
        brandTextCol.add(brandTagline);

        brandRow.add(brandTextCol);
        content.add(brandRow);
        content.add(Box.createVerticalStrut(22));

        // 2. Concise Intro
        JLabel introLabel = new JLabel("<html><div style='width:330px;line-height:1.45;color:#94A3B8;font-size:12.5px;'>"
                + "Discover campus events, connect with student communities, and explore opportunities beyond your college."
                + "</div></html>");
        introLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(introLabel);
        content.add(Box.createVerticalStrut(32));

        // 3. Three Short Feature Highlights
        JPanel featuresBox = new JPanel();
        featuresBox.setOpaque(false);
        featuresBox.setLayout(new BoxLayout(featuresBox, BoxLayout.Y_AXIS));
        featuresBox.setAlignmentX(LEFT_ALIGNMENT);

        addFeatureItem(featuresBox, "✦", "Discover Events",
                "Explore hackathons, workshops, cultural festivals, competitions, and campus activities.");
        featuresBox.add(Box.createVerticalStrut(18));

        addFeatureItem(featuresBox, "❖", "Connect with Communities",
                "Discover clubs, meet students, and participate in college communities.");
        featuresBox.add(Box.createVerticalStrut(18));

        addFeatureItem(featuresBox, "📌", "Stay Connected",
                "Bookmark events, manage registrations, and receive important campus updates.");

        content.add(featuresBox);
        hero.add(content, BorderLayout.CENTER);

        // Bottom Institutional Trust Note
        JLabel trustNote = new JLabel("Verified Multi-Campus Network · Discover Opportunities");
        trustNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trustNote.setForeground(new Color(100, 116, 139));
        hero.add(trustNote, BorderLayout.SOUTH);

        return hero;
    }

    private void addFeatureItem(JPanel parent, String iconGlyph, String title, String description) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        // Icon Badge
        JPanel iconBadge = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(51, 65, 85));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconBadge.setPreferredSize(new Dimension(34, 34));
        iconBadge.setMaximumSize(new Dimension(34, 34));
        iconBadge.setOpaque(false);

        JLabel glyphLabel = new JLabel(iconGlyph, JLabel.CENTER);
        glyphLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        glyphLabel.setForeground(new Color(56, 189, 248));
        iconBadge.add(glyphLabel, BorderLayout.CENTER);

        // Text Stack
        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(new Color(241, 245, 249));

        JLabel descLbl = new JLabel("<html><div style='width:280px;line-height:1.35;color:#94A3B8;font-size:11.5px;'>"
                + description + "</div></html>");

        textCol.add(titleLbl);
        textCol.add(Box.createVerticalStrut(2));
        textCol.add(descLbl);

        row.add(iconBadge, BorderLayout.WEST);
        row.add(textCol, BorderLayout.CENTER);

        parent.add(row);
    }

    /**
     * Right login panel with centered, modern navy login card.
     */
    private JPanel buildRightLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(11, 15, 23)); // Dark background

        // Card Container
        RoundedCard card = new RoundedCard(16, new Color(22, 30, 46), new Color(38, 50, 74));
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(430, 520));
        card.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.weightx = 1;

        int row = 0;

        // 1. Heading: Welcome back
        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        gbc.gridy = row++;
        card.add(heading, gbc);

        // 2. Subtitle
        JLabel subtitle = new JLabel("Sign in to continue exploring your campus.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(148, 163, 184)); // Muted blue-grey
        gbc.gridy = row++;
        card.add(subtitle, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(18), gbc);

        // 3. Username or institutional email label
        JLabel usernameLabel = new JLabel("Username or institutional email");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(226, 232, 240));
        gbc.gridy = row++;
        card.add(usernameLabel, gbc);

        // 4. Username field
        usernameField.setPreferredSize(new Dimension(0, 40));
        applyFieldStyle(usernameField);
        gbc.gridy = row++;
        card.add(usernameField, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(12), gbc);

        // 5. Password label + Forgot password row
        JPanel passLabelRow = new JPanel(new BorderLayout());
        passLabelRow.setOpaque(false);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(226, 232, 240));

        JLabel forgotLink = linkLabel("Forgot password?");
        forgotLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLink.setForeground(new Color(96, 165, 250));
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgotPasswordDialog(LoginFrame.this).setVisible(true);
            }
        });

        passLabelRow.add(passLabel, BorderLayout.WEST);
        passLabelRow.add(forgotLink, BorderLayout.EAST);
        gbc.gridy = row++;
        card.add(passLabelRow, gbc);

        // 6. Password field with working Show/Hide control
        JPanel passWrap = buildPasswordFieldWithToggle();
        gbc.gridy = row++;
        card.add(passWrap, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(18), gbc);

        // 7. Sign In prominent blue button
        loginBtn.setText("Sign In");
        loginBtn.setPreferredSize(new Dimension(0, 44));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setCornerRadius(8);
        loginBtn.addActionListener(e -> attemptLogin());
        gbc.gridy = row++;
        card.add(loginBtn, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(20), gbc);

        // 8. Secondary Links
        JPanel linksBox = new JPanel();
        linksBox.setOpaque(false);
        linksBox.setLayout(new BoxLayout(linksBox, BoxLayout.Y_AXIS));

        // Create Account Row
        JPanel studentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        studentRow.setOpaque(false);
        JLabel newPrompt = new JLabel("New to CampusConnect?");
        newPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newPrompt.setForeground(new Color(148, 163, 184));

        JLabel createLink = linkLabel("Create an account");
        createLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createLink.setForeground(new Color(96, 165, 250));
        createLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StudentRegistrationDialog reg = new StudentRegistrationDialog(LoginFrame.this);
                reg.setVisible(true);
                if (reg.isRegistered()) {
                    usernameField.setText(reg.getRegisteredUsername());
                    passwordField.requestFocusInWindow();
                }
            }
        });

        studentRow.add(newPrompt);
        studentRow.add(createLink);
        linksBox.add(studentRow);

        linksBox.add(Box.createVerticalStrut(8));

        // Admin Request Row
        JPanel adminRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        adminRow.setOpaque(false);
        JLabel repPrompt = new JLabel("College representative?");
        repPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        repPrompt.setForeground(new Color(148, 163, 184));

        JLabel adminLink = linkLabel("Request admin access");
        adminLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        adminLink.setForeground(new Color(96, 165, 250));
        adminLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminRequestDialog(LoginFrame.this).setVisible(true);
            }
        });

        adminRow.add(repPrompt);
        adminRow.add(adminLink);
        linksBox.add(adminRow);

        gbc.gridy = row;
        card.add(linksBox, gbc);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildPasswordFieldWithToggle() {
        final JPanel passWrap = new JPanel(new BorderLayout(0, 0));
        passWrap.setOpaque(true);
        passWrap.setBackground(new Color(15, 23, 42)); // #0F172A
        passWrap.setPreferredSize(new Dimension(0, 40));

        final Border normalBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));
        final Border focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(37, 99, 235), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));

        passWrap.setBorder(normalBorder);

        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(15, 23, 42));
        passwordField.setCaretColor(new Color(56, 189, 248));
        passwordField.setEchoChar('•');
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passWrap.setBorder(focusBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                passWrap.setBorder(normalBorder);
            }
        });

        showHideBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        showHideBtn.setForeground(new Color(148, 163, 184));
        showHideBtn.setOpaque(false);
        showHideBtn.setContentAreaFilled(false);
        showHideBtn.setBorderPainted(false);
        showHideBtn.setFocusPainted(false);
        showHideBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 12));

        showHideBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showHideBtn.setForeground(new Color(96, 165, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showHideBtn.setForeground(new Color(148, 163, 184));
            }
        });

        showHideBtn.addActionListener(e -> togglePasswordVisibility());

        passWrap.add(passwordField, BorderLayout.CENTER);
        passWrap.add(showHideBtn, BorderLayout.EAST);

        return passWrap;
    }

    private void applyFieldStyle(final JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(15, 23, 42));
        field.setCaretColor(new Color(56, 189, 248));

        final Border normalBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12));
        final Border focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(37, 99, 235), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12));

        field.setBorder(normalBorder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(focusBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(normalBorder);
            }
        });
    }

    private JLabel linkLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setText("<html><u>" + text + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setText(text);
            }
        });
        return label;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passwordField.setEchoChar((char) 0);
            showHideBtn.setText("Hide");
        } else {
            passwordField.setEchoChar('•');
            showHideBtn.setText("Show");
        }
        passwordField.requestFocusInWindow();
    }

    private void attemptLogin() {
        if (isLoggingIn) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username or registered email and password.",
                    "Sign In", JOptionPane.WARNING_MESSAGE);
            if (username.isEmpty()) {
                usernameField.requestFocusInWindow();
            } else {
                passwordField.requestFocusInWindow();
            }
            return;
        }

        isLoggingIn = true;
        loginBtn.setEnabled(false);

        try {
            User user = DummyData.findUser(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials. Please verify your username or institutional email and password.",
                        "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocusInWindow();
                return;
            }

            dispose();
            new MainFrame(user).setVisible(true);
        } finally {
            isLoggingIn = false;
            loginBtn.setEnabled(true);
        }
    }

    /**
     * Polished antialiased rounded logo mark component.
     */
    private static class LogoMark extends JComponent {
        LogoMark() {
            setPreferredSize(new Dimension(42, 42));
            setMaximumSize(new Dimension(42, 42));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(0, 0, new Color(37, 99, 235), 42, 42, new Color(79, 70, 229));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, 42, 42, 12, 12);

            g2.setColor(new Color(255, 255, 255, 70));
            g2.drawRoundRect(0, 0, 41, 41, 12, 12);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
            FontMetrics fm = g2.getFontMetrics();
            String glyph = "CC";
            int gx = (42 - fm.stringWidth(glyph)) / 2;
            int gy = (42 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(glyph, gx, gy);

            g2.dispose();
        }
    }
}
