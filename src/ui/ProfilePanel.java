package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Redesigned, comprehensive Profile screen with structured personal, academic,
 * institutional, and account sections, supporting view and validated edit modes.
 */
public class ProfilePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel container;
    private boolean isEditing = false;

    // Editable fields
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> interestBox = new JComboBox<String>(new String[] {
            "Technology", "Cultural", "Finance", "Sports", "Arts", "General"
    });
    // Student fields
    private final JTextField courseField = new JTextField();
    private final JTextField branchField = new JTextField();
    private final JTextField yearSemesterField = new JTextField();
    private final JTextField gradYearField = new JTextField();
    // Admin fields
    private final JTextField designationField = new JTextField();
    private final ImagePickerField avatarPicker = new ImagePickerField();

    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(AppTheme.BG);
        container.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        add(UiUtil.scroll(container), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        container.removeAll();
        User user = mainFrame.getCurrentUser();
        if (user == null) {
            return;
        }

        // 1. PROFILE HEADER CARD
        container.add(buildProfileHeader(user));
        container.add(Box.createVerticalStrut(20));

        // 2. SECTIONS
        container.add(buildPersonalInfoSection(user));
        container.add(Box.createVerticalStrut(18));

        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            container.add(buildAcademicSection(user));
            container.add(Box.createVerticalStrut(18));
        } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            container.add(buildInstitutionalSection(user));
            container.add(Box.createVerticalStrut(18));
        } else if ("SUPER_ADMIN".equalsIgnoreCase(user.getRole())) {
            container.add(buildSuperAdminSection(user));
            container.add(Box.createVerticalStrut(18));
        }

        container.add(buildAccountSection(user));

        container.revalidate();
        container.repaint();
    }

    private JPanel buildProfileHeader(User user) {
        RoundedCard card = new RoundedCard(16, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new BorderLayout(20, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (user.hasImage() && ImageUtil.isValidImage(user.getImagePath())) {
            javax.swing.ImageIcon customAvatar = ImageUtil.createAvatarIcon(user.getImagePath(), 72);
            if (customAvatar != null) {
                JLabel avatarLbl = new JLabel(customAvatar);
                card.add(avatarLbl, BorderLayout.WEST);
            } else {
                InitialsAvatar avatar = new InitialsAvatar(user.getFullName(), 72, AppTheme.NAVY, AppTheme.ACCENT);
                card.add(avatar, BorderLayout.WEST);
            }
        } else {
            InitialsAvatar avatar = new InitialsAvatar(user.getFullName(), 72, AppTheme.NAVY, AppTheme.ACCENT);
            card.add(avatar, BorderLayout.WEST);
        }

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nameRow.setOpaque(false);

        JLabel nameLbl = new JLabel(user.getFullName());
        nameLbl.setFont(AppTheme.TITLE.deriveFont(22f));
        nameLbl.setForeground(AppTheme.NAVY);
        nameRow.add(nameLbl);

        nameRow.add(PillBadge.role(user.getRole()));

        PillBadge statusBadge = new PillBadge("Active Account",
                new Color(236, 248, 240), AppTheme.SUCCESS, new Color(190, 230, 205));
        nameRow.add(statusBadge);

        details.add(nameRow);
        details.add(Box.createVerticalStrut(4));

        String collegeTag = user.getCollege() != null && !user.getCollege().isEmpty()
                ? "🏛  " + user.getCollege() : "CampusConnect Member";
        JLabel collegeLbl = new JLabel(collegeTag + "   ·   ✉  " + user.getEmail());
        collegeLbl.setFont(AppTheme.BODY);
        collegeLbl.setForeground(AppTheme.MUTED);
        details.add(collegeLbl);

        card.add(details, BorderLayout.CENTER);

        // Header Actions (Edit / Save / Cancel)
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 20));
        actions.setOpaque(false);

        if (!isEditing) {
            ModernButton editBtn = new ModernButton("✎  Edit Profile", ModernButton.Variant.PRIMARY);
            editBtn.setCornerRadius(8);
            editBtn.addActionListener(e -> {
                isEditing = true;
                populateEditFields(user);
                refresh();
            });
            actions.add(editBtn);
        } else {
            ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
            cancelBtn.setCornerRadius(8);
            cancelBtn.addActionListener(e -> {
                isEditing = false;
                refresh();
            });

            ModernButton saveBtn = new ModernButton("Save Changes", ModernButton.Variant.SUCCESS);
            saveBtn.setCornerRadius(8);
            saveBtn.addActionListener(e -> saveProfileChanges(user));

            actions.add(cancelBtn);
            actions.add(saveBtn);
        }

        card.add(actions, BorderLayout.EAST);
        return card;
    }

    private void populateEditFields(User user) {
        nameField.setText(user.getFullName() != null ? user.getFullName() : "");
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        phoneField.setText(user.getPhone() != null ? user.getPhone() : "");
        if (user.getInterest() != null) {
            interestBox.setSelectedItem(user.getInterest());
        }

        courseField.setText(user.getCourse() != null ? user.getCourse() : "");
        branchField.setText(user.getBranch() != null ? user.getBranch() : "");
        yearSemesterField.setText(user.getYearSemester() != null ? user.getYearSemester() : "");
        gradYearField.setText(user.getGraduationYear() != null ? user.getGraduationYear() : "");

        designationField.setText(user.getDesignation() != null ? user.getDesignation() : "");
        avatarPicker.setImagePath(user.getImagePath());
    }

    private JPanel buildPersonalInfoSection(User user) {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, isEditing ? 320 : 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 0.5;

        // Section Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(sectionHeading("Personal Information"), gbc);
        gbc.gridwidth = 1;

        if (!isEditing) {
            gbc.gridx = 0; gbc.gridy = 1;
            card.add(displayField("Full Name", user.getFullName()), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            card.add(displayField("Email Address", user.getEmail()), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            card.add(displayField("Contact Number", user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "Not specified"), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            card.add(displayField("Primary Interest", user.getInterest() != null ? user.getInterest() : "General"), gbc);
        } else {
            AppTheme.styleField(nameField);
            AppTheme.styleField(emailField);
            AppTheme.styleField(phoneField);
            AppTheme.styleComboBox(interestBox);

            gbc.gridx = 0; gbc.gridy = 1;
            card.add(inputField("Full Name *", nameField), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            card.add(inputField("Email Address *", emailField), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            card.add(inputField("Contact Number", phoneField), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            card.add(inputField("Primary Interest", interestBox), gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            card.add(inputField("Profile Photo (Optional)", avatarPicker), gbc);
            gbc.gridwidth = 1;
        }

        return card;
    }

    private JPanel buildAcademicSection(User user) {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 0.5;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(sectionHeading("Academic Information"), gbc);
        gbc.gridwidth = 1;

        // Note: College is protected and cannot be modified arbitrarily
        if (!isEditing) {
            gbc.gridx = 0; gbc.gridy = 1;
            card.add(displayField("Enrolled College", user.getCollege()), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            card.add(displayField("Course / Programme", user.getCourse().isEmpty() ? "B.Tech Computer Science" : user.getCourse()), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            card.add(displayField("Branch / Department", user.getBranch().isEmpty() ? "Engineering" : user.getBranch()), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            card.add(displayField("Academic Year & Semester", user.getYearSemester().isEmpty() ? "Year 3, Semester 6" : user.getYearSemester()), gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            card.add(displayField("Expected Graduation Year", user.getGraduationYear().isEmpty() ? "2027" : user.getGraduationYear()), gbc);
        } else {
            AppTheme.styleField(courseField);
            AppTheme.styleField(branchField);
            AppTheme.styleField(yearSemesterField);
            AppTheme.styleField(gradYearField);

            gbc.gridx = 0; gbc.gridy = 1;
            card.add(displayField("Enrolled College (Institutional)", user.getCollege() + " (Verified)"), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            card.add(inputField("Course / Programme", courseField), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            card.add(inputField("Branch / Department", branchField), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            card.add(inputField("Year & Semester", yearSemesterField), gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            card.add(inputField("Expected Graduation Year", gradYearField), gbc);
        }

        return card;
    }

    private JPanel buildInstitutionalSection(User user) {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(sectionHeading("Institutional Assignment"), gbc);
        gbc.gridwidth = 1;

        if (!isEditing) {
            gbc.gridx = 0; gbc.gridy = 1;
            card.add(displayField("Assigned College", user.getCollege()), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            String desig = (user.getDesignation() != null && !user.getDesignation().isEmpty()) ? user.getDesignation() : "College Administrator";
            card.add(displayField("Official Designation", desig), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            card.add(displayField("Administrative Role", "College Administrator"), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            card.add(displayField("Assigned Scope", "Events, Clubs & Notices Management for " + user.getCollege()), gbc);
        } else {
            AppTheme.styleField(designationField);

            gbc.gridx = 0; gbc.gridy = 1;
            card.add(displayField("Assigned College (Protected)", user.getCollege() + " (Institutional)"), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            card.add(inputField("Official Designation", designationField), gbc);
        }

        return card;
    }

    private JPanel buildSuperAdminSection(User user) {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(sectionHeading("System Governance & Permissions"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        card.add(displayField("Authority Scope", "Global Platform Administration"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        card.add(displayField("Approval Rights", "Full Administrator Onboarding & Revocation"), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(displayField("Institution Management", "All Registered Partner Campuses"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        card.add(displayField("Audit Status", "Verified Root Administrator"), gbc);

        return card;
    }

    private JPanel buildAccountSection(User user) {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new GridBagLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(sectionHeading("Account Information"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        card.add(displayField("Username (Permanent ID)", user.getUsername()), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        card.add(displayField("Role", user.getRole()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(displayField("Account Status", "Active & Verified"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        card.add(displayField("Data Storage", "In-Memory Session Store"), gbc);

        return card;
    }

    private JLabel sectionHeading(String title) {
        JLabel heading = new JLabel(title);
        heading.setFont(AppTheme.HEADING.deriveFont(15f));
        heading.setForeground(AppTheme.NAVY);
        return heading;
    }

    private JPanel displayField(String label, String value) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        lbl.setForeground(AppTheme.MUTED);

        JLabel val = new JLabel(value != null && !value.isEmpty() ? value : "—");
        val.setFont(AppTheme.BODY.deriveFont(Font.BOLD));
        val.setForeground(AppTheme.TEXT);

        p.add(lbl);
        p.add(Box.createVerticalStrut(3));
        p.add(val);
        return p;
    }

    private JPanel inputField(String label, Component field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        lbl.setForeground(AppTheme.NAVY);

        p.add(lbl);
        p.add(Box.createVerticalStrut(3));
        p.add(field);
        return p;
    }

    private void saveProfileChanges(User user) {
        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newInterest = String.valueOf(interestBox.getSelectedItem());

        if (newName.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name and Email Address cannot be blank.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Apply changes to user object
        user.setFullName(newName);
        user.setEmail(newEmail);
        user.setPhone(newPhone);
        user.setInterest(newInterest);

        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            user.setCourse(courseField.getText().trim());
            user.setBranch(branchField.getText().trim());
            user.setYearSemester(yearSemesterField.getText().trim());
            user.setGraduationYear(gradYearField.getText().trim());
        } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            user.setDesignation(designationField.getText().trim());
        }

        user.setImagePath(avatarPicker.getImagePath());

        isEditing = false;
        mainFrame.refreshHeader();
        refresh();

        JOptionPane.showMessageDialog(this,
                "<html><b>Profile Updated Successfully!</b><br>Your profile changes have been saved.</html>",
                "Profile Saved", JOptionPane.INFORMATION_MESSAGE);
    }
}
