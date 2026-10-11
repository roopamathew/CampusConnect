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
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Modern light-themed CampusConnect login screen.
 *
 * Layout:
 *   LEFT  — Soft blue-gradient branding panel with logo, tagline, and feature highlights.
 *   RIGHT — White card with email + password fields, show/hide, Remember Me, Forgot Password,
 *           Sign In button, register link, and admin access link.
 *
 * Keyboard:
 *   Enter in email → focuses password.
 *   Enter in password OR on Sign In button → submits login.
 *   Duplicate-submission guard via isLoggingIn flag.
 */
public class LoginFrame extends JFrame {

    // ── Palette (light, self-contained – does not depend on AppTheme dark defaults) ──
    private static final Color BG          = new Color(248, 249, 252);   // off-white page
    private static final Color CARD_BG     = Color.WHITE;
    private static final Color BRAND_TOP   = new Color(37, 99, 235);     // #2563EB blue
    private static final Color BRAND_BOT   = new Color(79, 70, 229);     // #4F46E5 indigo
    private static final Color BRAND_PANEL = new Color(241, 245, 254);   // very light blue tint
    private static final Color ACCENT      = new Color(37, 99, 235);
    private static final Color ACCENT_HOV  = new Color(29, 78, 216);
    private static final Color TEXT_DARK   = new Color(15, 23, 42);
    private static final Color TEXT_MED    = new Color(71, 85, 105);
    private static final Color TEXT_MUTED  = new Color(148, 163, 184);
    private static final Color FIELD_BG    = new Color(250, 250, 252);
    private static final Color BORDER_NRM  = new Color(203, 213, 225);
    private static final Color BORDER_FOC  = ACCENT;
    private static final Color SUCCESS     = new Color(16, 185, 129);
    private static final Color DANGER      = new Color(220, 38, 38);
    private static final Color LINK        = ACCENT;

    private static final Pattern EMAIL_PAT =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ── Fields ──
    private final JTextField     emailField    = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton        showHideBtn   = new JButton("Show");
    private final JCheckBox      rememberMe    = new JCheckBox("Remember Me");
    private final ModernButton   loginBtn      = new ModernButton("Sign In", ModernButton.Variant.PRIMARY);
    private final ModernButton   googleBtn     = new ModernButton("Continue with Google", ModernButton.Variant.SECONDARY);
    private final JLabel         errorLabel    = new JLabel(" ");

    private boolean passwordVisible = false;
    private boolean isLoggingIn     = false;

    public LoginFrame() {
        setTitle("CampusConnect – Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1060, 660);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(880, 580));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildLeftBrandPanel(), BorderLayout.WEST);
        root.add(buildRightLoginPanel(), BorderLayout.CENTER);
        setContentPane(root);

        // ── Keyboard navigation ──
        emailField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> attemptLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  LEFT BRANDING PANEL
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildLeftBrandPanel() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Soft vertical blue gradient
                GradientPaint gp = new GradientPaint(
                        0, 0,          new Color(235, 242, 255),
                        0, getHeight(), new Color(220, 233, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Right edge separator
                g2.setColor(new Color(203, 213, 225));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
        };
        hero.setPreferredSize(new Dimension(440, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(44, 40, 40, 40));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // 1. Logo mark + brand name
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(LEFT_ALIGNMENT);
        brandRow.add(new LogoMark());

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel brandName = new JLabel("CampusConnect");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandName.setForeground(TEXT_DARK);
        brandText.add(brandName);

        JLabel tagline = new JLabel("Discover Beyond Your Campus");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tagline.setForeground(ACCENT);
        brandText.add(tagline);

        brandRow.add(brandText);
        content.add(brandRow);
        content.add(Box.createVerticalStrut(28));

        // 2. Intro text
        JLabel intro = new JLabel("<html><div style='width:310px;line-height:1.5;"
                + "color:#475569;font-size:12.5px;'>"
                + "Discover campus events, connect with student communities, "
                + "and explore opportunities beyond your college."
                + "</div></html>");
        intro.setAlignmentX(LEFT_ALIGNMENT);
        content.add(intro);
        content.add(Box.createVerticalStrut(36));

        // 3. Feature highlights
        JPanel features = new JPanel();
        features.setOpaque(false);
        features.setLayout(new BoxLayout(features, BoxLayout.Y_AXIS));
        features.setAlignmentX(LEFT_ALIGNMENT);

        addFeature(features, "📅", "Discover Events",
                "Hackathons, workshops, cultural fests, and competitions across colleges.");
        features.add(Box.createVerticalStrut(20));
        addFeature(features, "👥", "Connect with Communities",
                "Find clubs, meet students, and join college communities.");
        features.add(Box.createVerticalStrut(20));
        addFeature(features, "📌", "Stay Connected",
                "Bookmark events, manage registrations, and get campus notices.");

        content.add(features);
        hero.add(content, BorderLayout.CENTER);

        // Bottom trust note
        JLabel trust = new JLabel("Verified Multi-Campus Network · Secure Login");
        trust.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trust.setForeground(TEXT_MUTED);
        hero.add(trust, BorderLayout.SOUTH);

        return hero;
    }

    private void addFeature(JPanel parent, String icon, String title, String desc) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Icon badge
        JPanel badge = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(203, 213, 225));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setPreferredSize(new Dimension(36, 36));
        badge.setMaximumSize(new Dimension(36, 36));
        badge.setOpaque(false);

        JLabel iconLbl = new JLabel(icon, JLabel.CENTER);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        badge.add(iconLbl, BorderLayout.CENTER);

        // Text
        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(TEXT_DARK);

        JLabel descLbl = new JLabel("<html><div style='width:260px;line-height:1.35;"
                + "color:#64748B;font-size:11.5px;'>" + desc + "</div></html>");

        textCol.add(titleLbl);
        textCol.add(Box.createVerticalStrut(2));
        textCol.add(descLbl);

        row.add(badge, BorderLayout.WEST);
        row.add(textCol, BorderLayout.CENTER);
        parent.add(row);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  RIGHT LOGIN PANEL
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildRightLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        // White card
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(4, 6, getWidth() - 4, getHeight() - 4, 18, 18);
                // White card
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 18, 18);
                // Border
                g2.setColor(BORDER_NRM);
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 18, 18);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 520));
        card.setBorder(BorderFactory.createEmptyBorder(32, 38, 32, 38));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.insets = new Insets(4, 0, 4, 0);
        int row = 0;

        // Heading
        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_DARK);
        g.gridy = row++; card.add(heading, g);

        // Subtitle
        JLabel sub = new JLabel("Sign in to your CampusConnect account");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_MED);
        g.gridy = row++; card.add(sub, g);

        g.gridy = row++; card.add(Box.createVerticalStrut(12), g);

        // ── Email field ──
        g.gridy = row++; card.add(fieldLabel("Email address"), g);
        applyFieldStyle(emailField, "Enter your email");
        g.gridy = row++; card.add(emailField, g);
        g.gridy = row++; card.add(Box.createVerticalStrut(4), g);

        // ── Password label + Forgot link ──
        JPanel passLabelRow = new JPanel(new BorderLayout());
        passLabelRow.setOpaque(false);
        passLabelRow.add(fieldLabel("Password"), BorderLayout.WEST);
        JLabel forgotLink = linkLabel("Forgot password?");
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new ForgotPasswordDialog(LoginFrame.this).setVisible(true);
            }
        });
        passLabelRow.add(forgotLink, BorderLayout.EAST);
        g.gridy = row++; card.add(passLabelRow, g);

        // ── Password field with show/hide ──
        g.gridy = row++; card.add(buildPasswordRow(), g);
        g.gridy = row++; card.add(Box.createVerticalStrut(2), g);

        // ── Remember Me ──
        rememberMe.setOpaque(false);
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMe.setForeground(TEXT_MED);
        rememberMe.setFocusPainted(false);
        g.gridy = row++; card.add(rememberMe, g);

        // ── Error label ──
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER);
        g.gridy = row++; card.add(errorLabel, g);
        g.gridy = row++; card.add(Box.createVerticalStrut(2), g);

        // ── Sign In button ──
        loginBtn.setText("Sign In");
        loginBtn.setPreferredSize(new Dimension(0, 42));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setCornerRadius(10);
        loginBtn.addActionListener(e -> attemptLogin());
        g.gridy = row++; card.add(loginBtn, g);
        g.gridy = row++; card.add(Box.createVerticalStrut(6), g);

        // ── Continue with Google button ──
        googleBtn.setText("Continue with Google");
        googleBtn.setPreferredSize(new Dimension(0, 42));
        googleBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        googleBtn.setCornerRadius(10);
        googleBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Google authentication is configured for institutional SSO.\nPlease use your institutional email and password to sign in.",
                    "Institutional Sign-In", JOptionPane.INFORMATION_MESSAGE);
        });
        g.gridy = row++; card.add(googleBtn, g);
        g.gridy = row++; card.add(Box.createVerticalStrut(14), g);

        // ── Secondary links ──
        JPanel linksPanel = new JPanel();
        linksPanel.setOpaque(false);
        linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));

        JPanel newRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        newRow.setOpaque(false);
        JLabel newPrompt = new JLabel("New to CampusConnect?");
        newPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newPrompt.setForeground(TEXT_MED);
        JLabel createLink = linkLabel("Create an account");
        createLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                StudentRegistrationDialog reg = new StudentRegistrationDialog(LoginFrame.this);
                reg.setVisible(true);
                if (reg.isRegistered()) {
                    emailField.setText(reg.getRegisteredUsername());
                    passwordField.requestFocusInWindow();
                }
            }
        });
        newRow.add(newPrompt); newRow.add(createLink);
        linksPanel.add(newRow);
        linksPanel.add(Box.createVerticalStrut(8));

        JPanel adminRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        adminRow.setOpaque(false);
        JLabel repPrompt = new JLabel("College representative?");
        repPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        repPrompt.setForeground(TEXT_MED);
        JLabel adminLink = linkLabel("Request admin access");
        adminLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        adminLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new AdminRequestDialog(LoginFrame.this).setVisible(true);
            }
        });
        adminRow.add(repPrompt); adminRow.add(adminLink);
        linksPanel.add(adminRow);

        g.gridy = row; card.add(linksPanel, g);

        wrapper.add(card);
        return wrapper;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    private void applyFieldStyle(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(TEXT_DARK);
        field.setBackground(FIELD_BG);
        field.setCaretColor(ACCENT);
        field.setPreferredSize(new Dimension(0, 42));

        final Border normal = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_NRM, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12));
        final Border focus = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_FOC, 2),
                BorderFactory.createEmptyBorder(7, 11, 7, 11));
        field.setBorder(normal);

        // Placeholder
        field.setText(placeholder);
        field.setForeground(TEXT_MUTED);
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBorder(focus);
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBorder(normal);
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_MUTED);
                }
            }
        });
    }

    private JPanel buildPasswordRow() {
        JPanel wrap = new JPanel(new BorderLayout(0, 0));
        wrap.setOpaque(true);
        wrap.setBackground(FIELD_BG);
        wrap.setPreferredSize(new Dimension(0, 42));

        final Border normal = BorderFactory.createLineBorder(BORDER_NRM, 1);
        final Border focus  = BorderFactory.createLineBorder(BORDER_FOC, 2);
        wrap.setBorder(normal);

        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setForeground(TEXT_DARK);
        passwordField.setBackground(FIELD_BG);
        passwordField.setCaretColor(ACCENT);
        passwordField.setEchoChar('•');
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));

        // Placeholder behaviour for password field
        passwordField.setToolTipText("Enter your password");
        passwordField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { wrap.setBorder(focus); }
            @Override public void focusLost(FocusEvent e)   { wrap.setBorder(normal); }
        });

        // Show / Hide button
        showHideBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        showHideBtn.setForeground(TEXT_MUTED);
        showHideBtn.setOpaque(false);
        showHideBtn.setContentAreaFilled(false);
        showHideBtn.setBorderPainted(false);
        showHideBtn.setFocusPainted(false);
        showHideBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 12));
        showHideBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { showHideBtn.setForeground(ACCENT); }
            @Override public void mouseExited(MouseEvent e)  { showHideBtn.setForeground(TEXT_MUTED); }
        });
        showHideBtn.addActionListener(e -> togglePasswordVisibility());

        wrap.add(passwordField, BorderLayout.CENTER);
        wrap.add(showHideBtn,   BorderLayout.EAST);
        return wrap;
    }


    private JLabel linkLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(LINK);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                lbl.setText("<html><u>" + text + "</u></html>");
            }
            @Override public void mouseExited(MouseEvent e) {
                lbl.setText(text);
            }
        });
        return lbl;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        passwordField.setEchoChar(passwordVisible ? (char) 0 : '•');
        showHideBtn.setText(passwordVisible ? "Hide" : "Show");
        passwordField.requestFocusInWindow();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  AUTHENTICATION
    // ─────────────────────────────────────────────────────────────────────────

    private void attemptLogin() {
        if (isLoggingIn) return;

        // Clear previous error
        errorLabel.setText(" ");

        String emailOrUser = emailField.getText().trim();
        String password    = new String(passwordField.getPassword());

        // Ignore placeholder text
        if ("Enter your email".equals(emailOrUser)) emailOrUser = "";

        // ── Validation ──
        if (emailOrUser.isEmpty()) {
            showError("Email address is required.");
            emailField.requestFocusInWindow();
            return;
        }
        if (password.isEmpty()) {
            showError("Password is required.");
            passwordField.requestFocusInWindow();
            return;
        }

        isLoggingIn = true;
        loginBtn.setEnabled(false);
        loginBtn.setText("Signing in…");

        // Run auth in same thread (dummy data is instant; replace with SwingWorker for real backend)
        try {
            User user = DummyData.findUser(emailOrUser, password);
            if (user == null) {
                showError("Invalid credentials. Please check your email and password.");
                passwordField.setText("");
                passwordField.requestFocusInWindow();
            } else {
                dispose();
                new MainFrame(user).setVisible(true);
            }
        } finally {
            isLoggingIn = false;
            loginBtn.setEnabled(true);
            loginBtn.setText("Sign In");
        }
    }

    private void showError(String message) {
        errorLabel.setForeground(DANGER);
        errorLabel.setText("⚠  " + message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  LOGO MARK COMPONENT
    // ─────────────────────────────────────────────────────────────────────────
    private static class LogoMark extends JComponent {
        LogoMark() {
            setPreferredSize(new Dimension(44, 44));
            setMaximumSize(new Dimension(44, 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(0, 0, BRAND_TOP, 44, 44, BRAND_BOT);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, 44, 44, 13, 13);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(0, 0, 43, 43, 13, 13);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
            FontMetrics fm = g2.getFontMetrics();
            String glyph = "CC";
            int gx = (44 - fm.stringWidth(glyph)) / 2;
            int gy = (44 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(glyph, gx, gy);

            g2.dispose();
        }
    }
}
