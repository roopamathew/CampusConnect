package ui;

import data.DummyData;
import model.AdminRequest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Dedicated form for college representatives to request administrator access.
 * Submissions enter the PENDING queue for Super Admin review.
 */
public class AdminRequestDialog extends JDialog {

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> collegeBox = new JComboBox<String>();
    private final JTextField designationField = new JTextField();
    private final JTextArea reasonArea = new JTextArea(4, 20);

    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public AdminRequestDialog(JFrame parent) {
        super(parent, "Request Administrator Access – CampusConnect", true);
        setSize(560, 640);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(500, 560));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel("College Administrator Access");
        title.setFont(AppTheme.TITLE);
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("For college faculty, department heads, and campus coordinators");
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

        for (String c : DummyData.getCollegeNames()) {
            collegeBox.addItem(c);
        }

        AppTheme.styleField(nameField);
        AppTheme.styleField(emailField);
        AppTheme.styleField(phoneField);
        AppTheme.styleField(designationField);
        AppTheme.styleComboBox(collegeBox);
        AppTheme.styleTextArea(reasonArea);

        int row = 0;
        row = addRow(form, gbc, row, "Applicant Full Name *", nameField);
        row = addRow(form, gbc, row, "Official Institutional Email *", emailField);
        row = addRow(form, gbc, row, "Contact Number *", phoneField);
        row = addRow(form, gbc, row, "Assigned College *", collegeBox);
        row = addRow(form, gbc, row, "Official Designation * (e.g. Dean, HOD, Faculty Advisor)", designationField);
        row = addRow(form, gbc, row, "Reason for Requesting Administrator Access *", new JScrollPane(reasonArea));

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

        ModernButton submitBtn = new ModernButton("Submit Request", ModernButton.Variant.PRIMARY);
        submitBtn.addActionListener(e -> attemptSubmit());

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

    private void attemptSubmit() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String college = String.valueOf(collegeBox.getSelectedItem());
        String designation = designationField.getText().trim();
        String reason = reasonArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || designation.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields marked with *.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid institutional email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DummyData.hasPendingRequestForEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "<html>A pending administrator request already exists for <b>" + email + "</b>.<br>"
                            + "Please await review by the Super Admin.</html>",
                    "Duplicate Pending Request", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DummyData.isEmailTaken(email)) {
            JOptionPane.showMessageDialog(this,
                    "An active account with this email address already exists. Please log in directly.",
                    "Account Exists", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AdminRequest newReq = new AdminRequest(DummyData.nextAdminRequestId(), name, email, phone,
                college, designation, reason, LocalDate.now(), "PENDING");
        DummyData.adminRequests.add(newReq);

        JOptionPane.showMessageDialog(this,
                "<html><b>Administrator Access Request Submitted!</b><br><br>"
                        + "Thank you, " + name + ".<br>"
                        + "Your application for <b>" + college + "</b> has been queued for Super Admin review.<br><br>"
                        + "<i>Note: In this in-memory prototype, administrator access requires approval via the Super Admin portal.</i></html>",
                "Request Received", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
