package ui;

import data.DummyData;
import model.Notice;
import model.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
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
 * Notice management panel scoped strictly to the authenticated administrator's college.
 */
public class ManageNoticesPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageNoticesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        User currentUser = mainFrame.getCurrentUser();
        String subtitle = (currentUser != null && !"SUPER_ADMIN".equals(currentUser.getRole()))
                ? "Official announcements and broadcasts for " + currentUser.getCollege()
                : "Platform-wide notice board management";

        JPanel header = UiUtil.pageHeader("Manage Campus Notices", subtitle, null);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {"ID", "Notice Title", "College", "Date Posted", "Priority / Type"}, 0) {
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

        ModernButton addBtn = new ModernButton("+ Add Notice", ModernButton.Variant.PRIMARY);
        ModernButton editBtn = new ModernButton("✎ Edit Selected", ModernButton.Variant.SECONDARY);
        ModernButton deleteBtn = new ModernButton("✕ Delete", ModernButton.Variant.DANGER);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            Notice notice = selected();
            if (notice != null) openForm(notice);
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

        List<Notice> list = new ArrayList<Notice>();
        for (Notice notice : DummyData.notices) {
            if (isSuper || (adminCollege != null && adminCollege.equalsIgnoreCase(notice.getCollege()))) {
                list.add(notice);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            Notice notice = list.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(notice.getNoticeId()),
                    notice.getTitle(),
                    notice.getCollege(),
                    notice.getPostedDate().toString(),
                    notice.getType()
            });
        }
    }

    private Notice selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a notice from the table first.",
                    "Manage Notices", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (Notice n : DummyData.notices) {
            if (n.getNoticeId() == id) {
                return n;
            }
        }
        return null;
    }

    private void deleteSelected() {
        Notice notice = selected();
        if (notice == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete notice: '" + notice.getTitle() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            DummyData.notices.remove(notice);
            refresh();
            JOptionPane.showMessageDialog(this, "Notice deleted successfully.",
                    "Delete Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void openForm(Notice existing) {
        final User user = mainFrame.getCurrentUser();
        final boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        final String adminCollege = user != null ? user.getCollege() : "Mar Baselios College";

        final JDialog dialog = new JDialog(mainFrame, existing == null ? "Create Campus Notice" : "Edit Notice", true);
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(AppTheme.NAVY);
        head.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel(existing == null ? "Publish New Notice" : "Edit Notice");
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

        JTextField titleField = new JTextField(existing == null ? "" : existing.getTitle());
        AppTheme.styleField(titleField);

        JTextArea descArea = new JTextArea(existing == null ? "" : existing.getDescription(), 4, 20);
        AppTheme.styleTextArea(descArea);

        JComboBox<String> typeBox = new JComboBox<String>(DummyData.NOTICE_TYPES);
        AppTheme.styleComboBox(typeBox);

        JComboBox<String> collegeBox = new JComboBox<String>(DummyData.getCollegeNames().toArray(new String[0]));
        AppTheme.styleComboBox(collegeBox);
        JTextField lockedCollege = new JTextField(adminCollege);
        AppTheme.styleField(lockedCollege);
        lockedCollege.setEditable(false);

        final ImagePickerField imagePicker = new ImagePickerField(existing != null ? existing.getImagePath() : "");

        if (existing != null) {
            typeBox.setSelectedItem(existing.getType());
            collegeBox.setSelectedItem(existing.getCollege());
        }

        int row = 0;
        row = addField(form, gbc, row, "Notice Title *", titleField);

        gbc.gridy = row++;
        JLabel descLabel = new JLabel("Announcement Body *");
        descLabel.setFont(AppTheme.BOLD);
        descLabel.setForeground(AppTheme.NAVY);
        form.add(descLabel, gbc);

        gbc.gridy = row++;
        form.add(new JScrollPane(descArea), gbc);

        row = addField(form, gbc, row, "Notice Priority / Type *", typeBox);

        if (isSuper) {
            row = addField(form, gbc, row, "Publishing College *", collegeBox);
        } else {
            row = addField(form, gbc, row, "Publishing College (Institutional Scope)", lockedCollege);
        }

        row = addField(form, gbc, row, "Notice Attachment Image (Optional)", imagePicker);

        root.add(new JScrollPane(form), BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actions.setBackground(AppTheme.CARD);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
        cancelBtn.addActionListener(e -> dialog.dispose());

        ModernButton saveBtn = new ModernButton("Save Notice", ModernButton.Variant.PRIMARY);
        saveBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String d = descArea.getText().trim();

            if (t.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String chosenCollege = isSuper ? String.valueOf(collegeBox.getSelectedItem()) : adminCollege;
            String chosenType = String.valueOf(typeBox.getSelectedItem());

            if (existing == null) {
                Notice created = new Notice(DummyData.nextNoticeId(), t, d, chosenCollege, LocalDate.now(), chosenType);
                created.setImagePath(imagePicker.getImagePath());
                DummyData.notices.add(created);
                JOptionPane.showMessageDialog(dialog, "Notice published successfully!", "Notice Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                existing.setTitle(t);
                existing.setDescription(d);
                if (isSuper) existing.setCollege(chosenCollege);
                existing.setType(chosenType);
                existing.setImagePath(imagePicker.getImagePath());
                JOptionPane.showMessageDialog(dialog, "Notice updated successfully!", "Notice Updated", JOptionPane.INFORMATION_MESSAGE);
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
