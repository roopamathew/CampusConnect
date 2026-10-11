package ui;

import data.DummyData;
import model.Club;
import model.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Club management panel scoped to the authenticated administrator's college.
 */
public class ManageClubsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageClubsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        User currentUser = mainFrame.getCurrentUser();
        String subtitle = (currentUser != null && !"SUPER_ADMIN".equals(currentUser.getRole()))
                ? "Manage clubs and activity circles hosted by " + currentUser.getCollege()
                : "Platform-wide club directory management";

        JPanel header = UiUtil.pageHeader("Manage Campus Clubs", subtitle, null);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {"ID", "Club Name", "College", "Category", "Contact Person", "Contact Email"}, 0) {
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

        ModernButton addBtn = new ModernButton("+ Add Club", ModernButton.Variant.PRIMARY);
        ModernButton editBtn = new ModernButton("✎ Edit Selected", ModernButton.Variant.SECONDARY);
        ModernButton deleteBtn = new ModernButton("✕ Delete", ModernButton.Variant.DANGER);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            Club club = selected();
            if (club != null) openForm(club);
        });
        deleteBtn.addActionListener(e -> deleteSelected());

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        add(buttons, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        User user = mainFrame.getCurrentUser();
        boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        String adminCollege = user != null ? user.getCollege() : "";

        List<Club> list = new ArrayList<Club>();
        for (Club club : DummyData.clubs) {
            if (isSuper || (adminCollege != null && adminCollege.equalsIgnoreCase(club.getCollege()))) {
                list.add(club);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            Club club = list.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(club.getClubId()),
                    club.getClubName(),
                    club.getCollege(),
                    club.getCategory(),
                    club.getContactPerson(),
                    club.getContactEmail()
            });
        }
    }

    private Club selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a club from the table first.",
                    "Manage Clubs", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (Club c : DummyData.clubs) {
            if (c.getClubId() == id) {
                return c;
            }
        }
        return null;
    }

    private void deleteSelected() {
        Club club = selected();
        if (club == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete club: '" + club.getClubName() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            DummyData.clubs.remove(club);
            refresh();
            JOptionPane.showMessageDialog(this, "Club deleted successfully.",
                    "Delete Club", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void openForm(Club existing) {
        final User user = mainFrame.getCurrentUser();
        final boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        final String adminCollege = user != null ? user.getCollege() : "Mar Baselios College";

        final JDialog dialog = new JDialog(mainFrame, existing == null ? "Register Campus Club" : "Edit Club", true);
        dialog.setSize(540, 560);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(AppTheme.NAVY);
        head.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel(existing == null ? "Add Campus Club" : "Edit Club: " + existing.getClubName());
        title.setFont(AppTheme.TITLE);
        title.setForeground(java.awt.Color.WHITE);
        head.add(title, BorderLayout.CENTER);
        root.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1;

        JTextField nameField = new JTextField(existing == null ? "" : existing.getClubName());
        AppTheme.styleField(nameField);

        JComboBox<String> categoryBox = new JComboBox<String>(DummyData.CLUB_CATEGORIES);
        AppTheme.styleComboBox(categoryBox);

        JComboBox<String> collegeBox = new JComboBox<String>(DummyData.getCollegeNames().toArray(new String[0]));
        AppTheme.styleComboBox(collegeBox);
        JTextField lockedCollege = new JTextField(adminCollege);
        AppTheme.styleField(lockedCollege);
        lockedCollege.setEditable(false);

        JTextArea descArea = new JTextArea(existing == null ? "" : existing.getDescription(), 4, 20);
        AppTheme.styleTextArea(descArea);

        JTextField contactField = new JTextField(existing == null ? (user != null ? user.getFullName() : "") : existing.getContactPerson());
        AppTheme.styleField(contactField);

        JTextField emailField = new JTextField(existing == null ? (user != null ? user.getEmail() : "") : existing.getContactEmail());
        AppTheme.styleField(emailField);

        if (existing != null) {
            categoryBox.setSelectedItem(existing.getCategory());
            collegeBox.setSelectedItem(existing.getCollege());
        }

        int row = 0;
        row = addField(form, gbc, row, "Club Name *", nameField);
        row = addField(form, gbc, row, "Category *", categoryBox);

        if (isSuper) {
            row = addField(form, gbc, row, "Host College *", collegeBox);
        } else {
            row = addField(form, gbc, row, "Host College (Institutional Assignment)", lockedCollege);
        }

        gbc.gridy = row++;
        JLabel descLabel = new JLabel("Description & Activities *");
        descLabel.setFont(AppTheme.BOLD);
        descLabel.setForeground(AppTheme.NAVY);
        form.add(descLabel, gbc);

        gbc.gridy = row++;
        form.add(new JScrollPane(descArea), gbc);

        row = addField(form, gbc, row, "Club Lead / Faculty Coordinator *", contactField);
        row = addField(form, gbc, row, "Official Contact Email *", emailField);

        root.add(new JScrollPane(form), BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actions.setBackground(AppTheme.CARD);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
        cancelBtn.addActionListener(e -> dialog.dispose());

        ModernButton saveBtn = new ModernButton("Save Club", ModernButton.Variant.PRIMARY);
        saveBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            String d = descArea.getText().trim();
            String cp = contactField.getText().trim();
            String em = emailField.getText().trim();

            if (n.isEmpty() || d.isEmpty() || cp.isEmpty() || em.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String chosenCollege = isSuper ? String.valueOf(collegeBox.getSelectedItem()) : adminCollege;
            String chosenCat = String.valueOf(categoryBox.getSelectedItem());

            if (existing == null) {
                DummyData.clubs.add(new Club(DummyData.nextClubId(), n, chosenCollege, chosenCat, d, cp, em));
                JOptionPane.showMessageDialog(dialog, "Club registered successfully!", "Club Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                existing.setClubName(n);
                if (isSuper) existing.setCollege(chosenCollege);
                existing.setCategory(chosenCat);
                existing.setDescription(d);
                existing.setContactPerson(cp);
                existing.setContactEmail(em);
                JOptionPane.showMessageDialog(dialog, "Club details updated successfully!", "Club Updated", JOptionPane.INFORMATION_MESSAGE);
            }

            refresh();
            dialog.dispose();
        });

        actions.add(cancelBtn);
        actions.add(saveBtn);
        root.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridy = row++;
        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.BOLD);
        lbl.setForeground(AppTheme.NAVY);
        form.add(lbl, gbc);

        gbc.gridy = row++;
        form.add(field, gbc);
        return row;
    }
}
