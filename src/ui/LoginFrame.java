package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginFrame() {
        setTitle("CampusConnect – Discover Beyond Your Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 580);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(720, 480));

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildBrandPanel(), BorderLayout.WEST);
        root.add(buildFormPanel(), BorderLayout.CENTER);
        setContentPane(root);

        getRootPane().setDefaultButton(null);
    }

    private JPanel buildBrandPanel() {
        JPanel brand = new JPanel(new GridBagLayout());
        brand.setBackground(AppTheme.NAVY);
        brand.setPreferredSize(new Dimension(380, 0));

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 24, 4, 24);

        JLabel name = new JLabel("CAMPUSCONNECT");
        name.setFont(AppTheme.BRAND);
        name.setForeground(Color.WHITE);
        name.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tagline = new JLabel("Discover Beyond Your Campus");
        tagline.setFont(AppTheme.BODY);
        tagline.setForeground(AppTheme.ACCENT);
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel about = new JLabel("<html><div style='text-align:center;width:260px;'>"
                + "Find hackathons, workshops, fests and notices from your college "
                + "and from campuses around you.</div></html>");
        about.setFont(AppTheme.SMALL);
        about.setForeground(new Color(203, 213, 225));
        about.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;
        inner.add(name, gbc);
        gbc.gridy = 1;
        inner.add(tagline, gbc);
        gbc.insets = new Insets(18, 24, 4, 24);
        gbc.gridy = 2;
        inner.add(about, gbc);

        brand.add(inner);
        return brand;
    }

    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(AppTheme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        AppTheme.styleCard(card);
        card.setPreferredSize(new Dimension(360, 380));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.weightx = 1;

        JLabel title = new JLabel("Welcome back");
        title.setFont(AppTheme.TITLE);
        title.setForeground(AppTheme.NAVY);

        JLabel hint = AppTheme.muted("Your gateway to campus opportunities");

        usernameField.setFont(AppTheme.BODY);
        passwordField.setFont(AppTheme.BODY);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JButton login = AppTheme.primaryButton("LOGIN");
        JButton exit = AppTheme.secondaryButton("EXIT");
        login.addActionListener(e -> attemptLogin());
        exit.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(login);

        int row = 0;
        gbc.gridy = row++;
        card.add(title, gbc);
        gbc.gridy = row++;
        card.add(hint, gbc);
        gbc.gridy = row++;
        card.add(label("Username"), gbc);
        gbc.gridy = row++;
        card.add(usernameField, gbc);
        gbc.gridy = row++;
        card.add(label("Password"), gbc);
        gbc.gridy = row++;
        card.add(passwordField, gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(16, 8, 6, 8);
        card.add(login, gbc);
        gbc.gridy = row;
        gbc.insets = new Insets(6, 8, 6, 8);
        card.add(exit, gbc);

        wrap.add(card);
        return wrap;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.BOLD);
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.",
                    "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }
        User user = DummyData.findUser(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
        new MainFrame(user).setVisible(true);
    }
}
