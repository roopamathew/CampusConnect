package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Redesigned, professional login screen for CampusConnect.
 * Features balanced split composition, username/email login, password show/hide,
 * student registration, admin access request, and quick demo role selection.
 */
public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(22);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JCheckBox rememberMeCheck = new JCheckBox("Remember me");
    private final JButton showHideBtn = new JButton("Show");
    private final ModernButton loginBtn = new ModernButton("Sign In", ModernButton.Variant.PRIMARY);

    private boolean passwordVisible = false;
    private boolean isLoggingIn = false;

    public LoginFrame() {
        setTitle("CampusConnect – Discover Beyond Your Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 640);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(880, 560));

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeroPanel(), BorderLayout.WEST);
        root.add(buildFormPanel(), BorderLayout.CENTER);
        setContentPane(root);

        // Keyboard actions
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> attemptLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    private JPanel buildHeroPanel() {
        JPanel hero = new JPanel(new BorderLayout());
        hero.setBackground(AppTheme.NAVY);
        hero.setPreferredSize(new Dimension(440, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(40, 36, 40, 36));

        // Top Branding
        JPanel topBox = new JPanel();
        topBox.setOpaque(false);
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));

        JLabel logoBadge = new JLabel("CAMPUSCONNECT");
        logoBadge.setFont(AppTheme.BRAND.deriveFont(24f));
        logoBadge.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Discover Beyond Your Campus");
        tagline.setFont(AppTheme.HEADING.deriveFont(Font.BOLD, 14f));
        tagline.setForeground(AppTheme.ACCENT);

        topBox.add(logoBadge);
        topBox.add(Box.createVerticalStrut(6));
        topBox.add(tagline);
        topBox.add(Box.createVerticalStrut(28));

        // Features list
        JPanel features = new JPanel();
        features.setOpaque(false);
        features.setLayout(new BoxLayout(features, BoxLayout.Y_AXIS));

        addFeatureBullet(features, "Inter-College Events", "Explore hackathons, workshops, and competitions hosted by top colleges.");
        addFeatureBullet(features, "Student Communities", "Connect with clubs, activity hubs, and campus societies across regions.");
        addFeatureBullet(features, "Direct Registrations", "One-click registration and saved bookmarks for upcoming dates.");
        addFeatureBullet(features, "Institutional Notices", "Verified administrative broadcasts and academic opportunities.");

        topBox.add(features);
        hero.add(topBox, BorderLayout.CENTER);

        // Bottom Demo Credentials Quick-Fill helper for Viva / Evaluation
        JPanel demoBox = new JPanel();
        demoBox.setOpaque(false);
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 30)),
                BorderFactory.createEmptyBorder(16, 0, 0, 0)));

        JLabel demoHint = new JLabel("Demo Accounts (Click to Fill):");
        demoHint.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        demoHint.setForeground(AppTheme.LAVENDER);
        demoBox.add(demoHint);
        demoBox.add(Box.createVerticalStrut(8));

        JPanel demoButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        demoButtons.setOpaque(false);

        ModernButton demoStudent = new ModernButton("Student", ModernButton.Variant.SECONDARY);
        demoStudent.setCornerRadius(6);
        demoStudent.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        demoStudent.addActionListener(e -> {
            usernameField.setText("student");
            passwordField.setText("student123");
            passwordField.requestFocusInWindow();
        });

        ModernButton demoAdmin = new ModernButton("College Admin", ModernButton.Variant.SECONDARY);
        demoAdmin.setCornerRadius(6);
        demoAdmin.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        demoAdmin.addActionListener(e -> {
            usernameField.setText("admin");
            passwordField.setText("admin123");
            passwordField.requestFocusInWindow();
        });

        ModernButton demoSuperAdmin = new ModernButton("Super Admin", ModernButton.Variant.ACCENT);
        demoSuperAdmin.setCornerRadius(6);
        demoSuperAdmin.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        demoSuperAdmin.addActionListener(e -> {
            usernameField.setText("superadmin");
            passwordField.setText("superadmin123");
            passwordField.requestFocusInWindow();
        });

        demoButtons.add(demoStudent);
        demoButtons.add(demoAdmin);
        demoButtons.add(demoSuperAdmin);
        demoBox.add(demoButtons);

        hero.add(demoBox, BorderLayout.SOUTH);
        return hero;
    }

    private void addFeatureBullet(JPanel parent, String title, String description) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JLabel bullet = new JLabel("◆");
        bullet.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bullet.setForeground(AppTheme.ACCENT);
        bullet.setVerticalAlignment(SwingConstants.TOP);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(AppTheme.BOLD);
        titleLbl.setForeground(Color.WHITE);

        JLabel descLbl = new JLabel("<html><div style='width:290px;color:#C4BAD8;font-size:11px;'>" + description + "</div></html>");
        descLbl.setFont(AppTheme.SMALL);

        text.add(titleLbl);
        text.add(descLbl);

        item.add(bullet, BorderLayout.WEST);
        item.add(text, BorderLayout.CENTER);

        parent.add(item);
        parent.add(Box.createVerticalStrut(14));
    }

    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(AppTheme.BG);

        RoundedCard card = new RoundedCard(16, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(460, 540));
        card.setBorder(BorderFactory.createEmptyBorder(28, 32, 24, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.weightx = 1;

        int row = 0;

        // Title
        JLabel title = new JLabel("Welcome Back");
        title.setFont(AppTheme.TITLE.deriveFont(22f));
        title.setForeground(AppTheme.NAVY);
        gbc.gridy = row++;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Sign in with your username or institutional email");
        subtitle.setFont(AppTheme.SMALL);
        subtitle.setForeground(AppTheme.MUTED);
        gbc.gridy = row++;
        card.add(subtitle, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(10), gbc);

        // Username / Email field
        JLabel userLabel = new JLabel("Username or Email");
        userLabel.setFont(AppTheme.BOLD);
        userLabel.setForeground(AppTheme.NAVY);
        gbc.gridy = row++;
        card.add(userLabel, gbc);

        AppTheme.styleField(usernameField);
        usernameField.setPreferredSize(new Dimension(0, 38));
        gbc.gridy = row++;
        card.add(usernameField, gbc);

        // Password field with Show/Hide toggle
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppTheme.BOLD);
        passLabel.setForeground(AppTheme.NAVY);
        gbc.gridy = row++;
        card.add(passLabel, gbc);

        JPanel passRow = new JPanel(new BorderLayout(4, 0));
        passRow.setOpaque(false);
        AppTheme.styleField(passwordField);
        passwordField.setPreferredSize(new Dimension(0, 38));

        showHideBtn.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        showHideBtn.setForeground(AppTheme.INDIGO);
        showHideBtn.setOpaque(false);
        showHideBtn.setContentAreaFilled(false);
        showHideBtn.setBorderPainted(false);
        showHideBtn.setFocusPainted(false);
        showHideBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBtn.addActionListener(e -> togglePasswordVisibility());

        passRow.add(passwordField, BorderLayout.CENTER);
        passRow.add(showHideBtn, BorderLayout.EAST);
        gbc.gridy = row++;
        card.add(passRow, gbc);

        // Remember Me & Forgot Password Row
        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);

        rememberMeCheck.setFont(AppTheme.SMALL);
        rememberMeCheck.setForeground(AppTheme.TEXT);
        rememberMeCheck.setOpaque(false);
        rememberMeCheck.setFocusPainted(false);

        JLabel forgotLink = linkLabel("Forgot password?");
        forgotLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new ForgotPasswordDialog(LoginFrame.this).setVisible(true);
            }
        });

        optionsRow.add(rememberMeCheck, BorderLayout.WEST);
        optionsRow.add(forgotLink, BorderLayout.EAST);
        gbc.gridy = row++;
        card.add(optionsRow, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(4), gbc);

        // Sign In Button
        loginBtn.setPreferredSize(new Dimension(0, 40));
        loginBtn.addActionListener(e -> attemptLogin());
        gbc.gridy = row++;
        card.add(loginBtn, gbc);

        // Google Sign In Option
        ModernButton googleBtn = new ModernButton("Continue with Google", ModernButton.Variant.SECONDARY);
        googleBtn.setPreferredSize(new Dimension(0, 38));
        googleBtn.addActionListener(e -> new GoogleAuthDialog(LoginFrame.this).setVisible(true));
        gbc.gridy = row++;
        card.add(googleBtn, gbc);

        gbc.gridy = row++;
        card.add(Box.createVerticalStrut(6), gbc);

        // Registration & Admin Request Links
        JPanel linksPanel = new JPanel();
        linksPanel.setOpaque(false);
        linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));

        JPanel studentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        studentRow.setOpaque(false);
        JLabel newPrompt = new JLabel("New student?");
        newPrompt.setFont(AppTheme.SMALL);
        newPrompt.setForeground(AppTheme.MUTED);
        JLabel createLink = linkLabel("Create an account");
        createLink.addMouseListener(new MouseAdapter() {
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
        linksPanel.add(studentRow);

        JPanel adminRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        adminRow.setOpaque(false);
        JLabel repPrompt = new JLabel("College representative?");
        repPrompt.setFont(AppTheme.SMALL);
        repPrompt.setForeground(AppTheme.MUTED);
        JLabel adminLink = linkLabel("Request Admin Access");
        adminLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new AdminRequestDialog(LoginFrame.this).setVisible(true);
            }
        });
        adminRow.add(repPrompt);
        adminRow.add(adminLink);
        linksPanel.add(adminRow);

        gbc.gridy = row;
        card.add(linksPanel, gbc);

        wrap.add(card);
        return wrap;
    }

    private JLabel linkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        label.setForeground(AppTheme.INDIGO);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                label.setText("<html><u>" + text + "</u></html>");
            }

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
    }

    private void attemptLogin() {
        if (isLoggingIn) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username or registered email and password.",
                    "Sign In", JOptionPane.WARNING_MESSAGE);
            return;
        }

        isLoggingIn = true;
        loginBtn.setEnabled(false);

        try {
            User user = DummyData.findUser(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials. Please verify your username/email and password.",
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
}
