package ui;

import data.DummyData;
import model.AdminRequest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Super Admin panel for reviewing, approving, and rejecting college administrator access applications.
 */
public class AdminRequestsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;
    private final AutoCompleteComboBox statusFilter = AutoCompleteComboBox.strict(new String[] {
            "Pending Only", "All Requests", "Approved", "Rejected"
    });
    private final JLabel countLabel = new JLabel();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMM yyyy");

    public AdminRequestsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel header = UiUtil.pageHeader("Administrator Access Requests",
                "Review and provision administrator credentials for college representatives", null);

        // Filter Bar Card
        RoundedCard filterBar = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        filterBar.setLayout(new BorderLayout(12, 0));
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel filterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterLeft.setOpaque(false);
        JLabel filterLbl = new JLabel("Filter Applications:");
        filterLbl.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        filterLbl.setForeground(AppTheme.NAVY);
        filterLeft.add(filterLbl);

        AppTheme.styleComboBox(statusFilter);
        statusFilter.setPreferredSize(new Dimension(180, 34));
        filterLeft.add(statusFilter);
        filterBar.add(filterLeft, BorderLayout.WEST);

        countLabel.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        countLabel.setForeground(AppTheme.MUTED);
        JPanel filterRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterRight.setOpaque(false);
        filterRight.add(countLabel);
        filterBar.add(filterRight, BorderLayout.EAST);

        JPanel northWrapper = new JPanel();
        northWrapper.setOpaque(false);
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.add(header);
        northWrapper.add(Box.createVerticalStrut(14));
        northWrapper.add(filterBar);
        add(northWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Req ID", "Applicant Name", "Official Email", "Contact", "Assigned College", "Designation", "Date", "Status"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.styleTable(table);

        JPanel tableWrapper = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        tableWrapper.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.CARD);
        tableWrapper.add(scroll, BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        // Buttons Bar
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);

        ModernButton approveBtn = new ModernButton("✓ Approve Request", ModernButton.Variant.SUCCESS);
        ModernButton rejectBtn = new ModernButton("✕ Reject Request", ModernButton.Variant.DANGER);
        ModernButton viewDetailsBtn = new ModernButton("View Full Application", ModernButton.Variant.PRIMARY);
        ModernButton refreshBtn = new ModernButton("Refresh", ModernButton.Variant.SECONDARY);

        approveBtn.addActionListener(e -> approveSelected());
        rejectBtn.addActionListener(e -> rejectSelected());
        viewDetailsBtn.addActionListener(e -> viewDetailsSelected());
        refreshBtn.addActionListener(e -> refresh());

        buttons.add(approveBtn);
        buttons.add(rejectBtn);
        buttons.add(viewDetailsBtn);
        buttons.add(refreshBtn);
        add(buttons, BorderLayout.SOUTH);

        statusFilter.addActionListener(e -> fillTable());
        refresh();
    }

    public void refresh() {
        fillTable();
    }

    private void fillTable() {
        model.setRowCount(0);
        String filter = statusFilter.getValue();
        int count = 0;

        for (AdminRequest req : DummyData.adminRequests) {
            boolean include = false;
            if ("Pending Only".equals(filter) && "PENDING".equalsIgnoreCase(req.getStatus())) include = true;
            else if ("All Requests".equals(filter)) include = true;
            else if ("Approved".equals(filter) && "APPROVED".equalsIgnoreCase(req.getStatus())) include = true;
            else if ("Rejected".equals(filter) && "REJECTED".equalsIgnoreCase(req.getStatus())) include = true;

            if (include) {
                model.addRow(new Object[] {
                        Integer.valueOf(req.getRequestId()),
                        req.getFullName(),
                        req.getEmail(),
                        req.getPhone(),
                        req.getCollege(),
                        req.getDesignation(),
                        req.getRequestDate().format(DATE_FMT),
                        req.getStatus()
                });
                count++;
            }
        }

        countLabel.setText("Showing: " + count + " applications");
    }

    private AdminRequest selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an applicant from the table first.",
                    "Select Request", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (AdminRequest r : DummyData.adminRequests) {
            if (r.getRequestId() == id) {
                return r;
            }
        }
        return null;
    }

    private void approveSelected() {
        AdminRequest req = selected();
        if (req == null) return;

        if (!"PENDING".equalsIgnoreCase(req.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "This application is already " + req.getStatus() + ".",
                    "Request Already Processed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "<html>Are you sure you want to approve administrator access for:<br><br>"
                        + "<b>Applicant:</b> " + req.getFullName() + "<br>"
                        + "<b>Official Email:</b> " + req.getEmail() + "<br>"
                        + "<b>Assigned Campus:</b> " + req.getCollege() + "<br>"
                        + "<b>Designation:</b> " + req.getDesignation() + "<br><br>"
                        + "Approving will generate an active <b>COLLEGE ADMIN</b> account with permissions scoped to <b>" + req.getCollege() + "</b>.</html>",
                "Confirm Administrator Approval", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean approved = DummyData.approveAdminRequest(req, "admin123");
            if (approved) {
                refresh();
                JOptionPane.showMessageDialog(this,
                        "<html><b>Administrator Account Successfully Provisioned!</b><br><br>"
                                + "Account Role: <b>ADMIN (College Administrator)</b><br>"
                                + "Assigned College: <b>" + req.getCollege() + "</b><br>"
                                + "Official Email: <b>" + req.getEmail() + "</b><br>"
                                + "Temporary Password: <b>admin123</b><br><br>"
                                + "<i>The administrator can now log in immediately using these credentials.</i></html>",
                        "Approval Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void rejectSelected() {
        AdminRequest req = selected();
        if (req == null) return;

        if (!"PENDING".equalsIgnoreCase(req.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "This application is already " + req.getStatus() + ".",
                    "Request Already Processed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String reason = JOptionPane.showInputDialog(this,
                "<html>Please enter the reason for rejecting <b>" + req.getFullName() + "</b>'s application:</html>",
                "Rejection Reason", JOptionPane.PLAIN_MESSAGE);

        if (reason != null && !reason.trim().isEmpty()) {
            DummyData.rejectAdminRequest(req, reason.trim());
            refresh();
            JOptionPane.showMessageDialog(this,
                    "Application marked as REJECTED. No administrator account was granted.",
                    "Application Rejected", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewDetailsSelected() {
        AdminRequest req = selected();
        if (req == null) return;

        JOptionPane.showMessageDialog(this,
                "<html><div style='width:420px;line-height:1.4;'>"
                        + "<b style='font-size:14px;color:#1A233A;'>Administrator Application #" + req.getRequestId() + "</b><br><br>"
                        + "<b>Applicant Name:</b> " + req.getFullName() + "<br>"
                        + "<b>Official Email:</b> " + req.getEmail() + "<br>"
                        + "<b>Contact Number:</b> " + req.getPhone() + "<br>"
                        + "<b>Assigned Campus:</b> " + req.getCollege() + "<br>"
                        + "<b>Official Title / Designation:</b> " + req.getDesignation() + "<br>"
                        + "<b>Application Date:</b> " + req.getRequestDate().format(DATE_FMT) + "<br>"
                        + "<b>Status:</b> " + req.getStatus() + "<br><br>"
                        + "<b>Stated Reason for Access:</b><br>"
                        + "<i>" + req.getReason() + "</i><br><br>"
                        + (req.getReviewerNotes() != null ? "<b>Reviewer Notes:</b> " + req.getReviewerNotes() + "<br>" : "")
                        + "</div></html>",
                "Application Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
