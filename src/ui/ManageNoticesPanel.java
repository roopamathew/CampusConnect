package ui;

import data.DummyData;
import model.Notice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ManageNoticesPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public ManageNoticesPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.BG);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel("Manage Notices");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);

        model = new DefaultTableModel(new String[] {"ID", "Title", "College", "Date", "Type"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(AppTheme.BODY);
        table.getTableHeader().setFont(AppTheme.BOLD);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        JButton add = AppTheme.primaryButton("Add");
        JButton edit = AppTheme.secondaryButton("Edit");
        JButton delete = AppTheme.dangerButton("Delete");
        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> {
            Notice notice = selected();
            if (notice != null) {
                openForm(notice);
            }
        });
        delete.addActionListener(e -> deleteSelected());
        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);

        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        for (int i = 0; i < DummyData.notices.size(); i++) {
            Notice notice = DummyData.notices.get(i);
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
            JOptionPane.showMessageDialog(this, "Please select a notice first.",
                    "Manage Notices", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (int i = 0; i < DummyData.notices.size(); i++) {
            if (DummyData.notices.get(i).getNoticeId() == id) {
                return DummyData.notices.get(i);
            }
        }
        return null;
    }

    private void deleteSelected() {
        Notice notice = selected();
        if (notice == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this notice?",
                "Delete Notice", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            DummyData.notices.remove(notice);
            refresh();
            JOptionPane.showMessageDialog(this, "Notice deleted successfully.",
                    "Delete Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openForm(Notice existing) {
        JTextField title = new JTextField(existing == null ? "" : existing.getTitle());
        JTextArea description = new JTextArea(existing == null ? "" : existing.getDescription(), 4, 20);
        JComboBox<String> college = new JComboBox<String>(DummyData.getCollegeNames().toArray(new String[0]));
        JComboBox<String> type = new JComboBox<String>(DummyData.NOTICE_TYPES);
        if (existing != null) {
            college.setSelectedItem(existing.getCollege());
            type.setSelectedItem(existing.getType());
        }
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Title"));
        form.add(title);
        form.add(new JLabel("Description"));
        form.add(new JScrollPane(description));
        form.add(new JLabel("College"));
        form.add(college);
        form.add(new JLabel("Type"));
        form.add(type);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add Notice" : "Edit Notice",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        if (title.getText().trim().isEmpty() || description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (existing == null) {
            DummyData.notices.add(new Notice(DummyData.nextNoticeId(), title.getText().trim(),
                    description.getText().trim(), String.valueOf(college.getSelectedItem()),
                    LocalDate.now(), String.valueOf(type.getSelectedItem())));
            JOptionPane.showMessageDialog(this, "Notice added successfully.",
                    "Add Notice", JOptionPane.INFORMATION_MESSAGE);
        } else {
            existing.setTitle(title.getText().trim());
            existing.setDescription(description.getText().trim());
            existing.setCollege(String.valueOf(college.getSelectedItem()));
            existing.setType(String.valueOf(type.getSelectedItem()));
            JOptionPane.showMessageDialog(this, "Notice updated successfully.",
                    "Edit Notice", JOptionPane.INFORMATION_MESSAGE);
        }
        refresh();
    }
}
