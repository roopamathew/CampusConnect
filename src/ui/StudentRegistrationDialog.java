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
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Dedicated student registration dialog.
 * Public registrations ALWAYS create STUDENT accounts.
 */
public class StudentRegistrationDialog extends JDialog {

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JComboBox<String> collegeBox = new JComboBox<String>();
    private final JTextField courseField = new JTextField("B.Tech Computer Science");
    private final JTextField branchField = new JTextField("Computer Science & Engineering");
    private final JTextField yearField = new JTextField("Year 1, Semester 1");
    private final JComboBox<String> interestBox = new JComboBox<String>(new String[] {
            "Technology", "Cultural", "Finance", "Sports", "Arts", "General"
    });

    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private boolean registered = false;
    private String registeredUsername = "";

    public StudentRegistrationDialog(JFrame parent) {
        super(parent, "Student Registration – CampusConnect", true);
        setSize(560, 680);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(500, 580));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel("Create Student Account");
        title.setFont(AppTheme.TITLE);
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("Join CampusConnect to discover inter-college events & clubs");
        subtitle.setFont(AppTheme.SMALL);
        subtitle.setForeground(AppTheme.LAVENDER);
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);
        header.add(titleBox, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Form inside scroll
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.weightx = 1;

        // Populate colleges
        for (String c : DummyData.getCollegeNames()) {
            collegeBox.addItem(c);
        }

        AppTheme.styleField(nameField);
        AppTheme.styleField(emailField);
        AppTheme.styleField(usernameField);
        AppTheme.styleField(passwordField);
        AppTheme.styleField(confirmPasswordField);
        AppTheme.styleField(courseField);
        AppTheme.styleField(branchField);
        AppTheme.styleField(yearField);
        AppTheme.styleComboBox(collegeBox);
        AppTheme.styleComboBox(interestBox);

        int row = 0;
        row = addRow(form, gbc, row, "Full Name *", nameField);
        row = addRow(form, gbc, row, "Student Email *", emailField);
        row = addRow(form, gbc, row, "Username *", usernameField);
        row = addRow(form, gbc, row, "Password *", passwordField);
        row = addRow(form, gbc, row, "Confirm Password *", confirmPasswordField);
        row = addRow(form, gbc, row, "College *", collegeBox);
        row = addRow(form, gbc, row, "Course / Programme", courseField);
        row = addRow(form, gbc, row, "Branch / Department", branchField);
        row = addRow(form, gbc, row, "Year & Semester", yearField);
        row = addRow(form, gbc, row, "Primary Interest", interestBox);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        footer.setBackground(AppTheme.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        ModernButton submitBtn = new ModernButton("Create Account", ModernButton.Variant.PRIMARY);
        submitBtn.addActionListener(e -> attemptRegister());

        footer.add(cancelBtn);
        footer.add(submitBtn);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private int addRow(JPanel form, GridBagConstraints gbc, int row, String labelText, java.awt.Component comp) {
        gbc.gridy = row++;
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.BOLD);
        label.setForeground(AppTheme.NAVY);
        form.add(label, gbc);

        gbc.gridy = row++;
        form.add(comp, gbc);
        return row;
    }

    private void attemptRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String college = String.valueOf(collegeBox.getSelectedItem());
        String course = courseField.getText().trim();
        String branch = branchField.getText().trim();
        String year = yearField.getText().trim();
        String interest = String.valueOf(interestBox.getSelectedItem());

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields marked with *.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.",
                    "Password Too Short", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match.",
                    "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DummyData.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "The username '" + username + "' is already taken. Please choose another.",
                    "Username Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DummyData.isEmailTaken(email)) {
            JOptionPane.showMessageDialog(this, "An account with email '" + email + "' already exists. Please log in instead.",
                    "Email Already Registered", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Public registrations MUST ALWAYS create STUDENT role
        User newUser = new User(username, password, "STUDENT", name, email, college, interest,
                course, branch, year, "2028", "Student", "");
        DummyData.users.add(newUser);

        registered = true;
        registeredUsername = username;

        JOptionPane.showMessageDialog(this,
                "<html><b>Account created successfully!</b><br><br>"
                        + "Welcome to CampusConnect, " + name + "!<br>"
                        + "You can now log in with username: <b>" + username + "</b></html>",
                "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }
}
