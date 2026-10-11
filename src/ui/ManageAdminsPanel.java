package ui;

import data.DummyData;
import model.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Super Admin panel for viewing, auditing, and managing college administrator accounts.
 */
public class ManageAdminsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageAdminsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel header = UiUtil.pageHeader("Administrator Account Governance",
                "Audit registered college representatives, assigned campuses, and administrative access levels.", null);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Username", "Full Name", "Official Email", "Assigned Campus", "Designation", "Role", "Status"
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

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);

        ModernButton viewBtn = new ModernButton("View Admin Profile", ModernButton.Variant.PRIMARY);
        ModernButton removeBtn = new ModernButton("Revoke Admin Access", ModernButton.Variant.DANGER);
        ModernButton refreshBtn = new ModernButton("Refresh", ModernButton.Variant.SECONDARY);

        viewBtn.addActionListener(e -> viewSelected());
        removeBtn.addActionListener(e -> removeSelected());
        refreshBtn.addActionListener(e -> refresh());

        buttons.add(viewBtn);
        buttons.add(removeBtn);
        buttons.add(refreshBtn);
        add(buttons, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        List<User> admins = new ArrayList<User>();
        for (User u : DummyData.users) {
            if ("ADMIN".equalsIgnoreCase(u.getRole()) || "SUPER_ADMIN".equalsIgnoreCase(u.getRole())) {
                admins.add(u);
            }
        }

        for (int i = 0; i < admins.size(); i++) {
            User a = admins.get(i);
            String desig = (a.getDesignation() != null && !a.getDesignation().isEmpty()) ? a.getDesignation() : "Coordinator";
            model.addRow(new Object[] {
                    a.getUsername(),
                    a.getFullName(),
                    a.getEmail(),
                    a.getCollege(),
                    desig,
                    a.getRole(),
                    a.getAccountStatus()
            });
        }
    }

    private User selectedUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an administrator row first.",
                    "Select Administrator", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String username = String.valueOf(model.getValueAt(row, 0));
        for (User u : DummyData.users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    private void viewSelected() {
        User u = selectedUser();
        if (u == null) return;

        JOptionPane.showMessageDialog(this,
                "<html><div style='width:380px;line-height:1.4;'>"
                        + "<b style='font-size:14px;color:#1A233A;'>Administrator Record</b><br><br>"
                        + "<b>Full Name:</b> " + u.getFullName() + "<br>"
                        + "<b>Username:</b> " + u.getUsername() + "<br>"
                        + "<b>Role:</b> " + u.getRole() + "<br>"
                        + "<b>Assigned Campus:</b> " + u.getCollege() + "<br>"
                        + "<b>Official Email:</b> " + u.getEmail() + "<br>"
                        + "<b>Designation:</b> " + u.getDesignation() + "<br>"
                        + "<b>Contact:</b> " + u.getPhone() + "<br>"
                        + "<b>Account Status:</b> " + u.getAccountStatus() + "<br>"
                        + "</div></html>",
                "Administrator Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeSelected() {
        User u = selectedUser();
        if (u == null) return;

        if ("SUPER_ADMIN".equalsIgnoreCase(u.getRole())) {
            JOptionPane.showMessageDialog(this, "Root Super Administrator accounts cannot be revoked.",
                    "Action Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "<html>Are you sure you want to revoke administrator access for:<br>"
                        + "<b>" + u.getFullName() + "</b> (" + u.getCollege() + ")?</html>",
                "Revoke Access", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            DummyData.users.remove(u);
            refresh();
            JOptionPane.showMessageDialog(this, "Administrator access has been revoked.",
                    "Access Revoked", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
