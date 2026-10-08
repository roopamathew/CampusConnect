package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ManageCollegesPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public ManageCollegesPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.BG);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel("Manage Colleges");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);

        model = new DefaultTableModel(new String[] {"ID", "College Name", "Location", "Email"}, 0) {
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
            College college = selected();
            if (college != null) {
                openForm(college);
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
        for (int i = 0; i < DummyData.colleges.size(); i++) {
            College college = DummyData.colleges.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(college.getCollegeId()),
                    college.getCollegeName(),
                    college.getLocation(),
                    college.getEmail()
            });
        }
    }

    private College selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a college first.",
                    "Manage Colleges", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (int i = 0; i < DummyData.colleges.size(); i++) {
            if (DummyData.colleges.get(i).getCollegeId() == id) {
                return DummyData.colleges.get(i);
            }
        }
        return null;
    }

    private void deleteSelected() {
        College college = selected();
        if (college == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this college?",
                "Delete College", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            DummyData.colleges.remove(college);
            refresh();
            JOptionPane.showMessageDialog(this, "College deleted successfully.",
                    "Delete College", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openForm(College existing) {
        JTextField name = new JTextField(existing == null ? "" : existing.getCollegeName());
        JTextField location = new JTextField(existing == null ? "" : existing.getLocation());
        JTextField email = new JTextField(existing == null ? "" : existing.getEmail());
        JTextArea description = new JTextArea(existing == null ? "" : existing.getDescription(), 3, 20);
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("College Name"));
        form.add(name);
        form.add(new JLabel("Location"));
        form.add(location);
        form.add(new JLabel("Email"));
        form.add(email);
        form.add(new JLabel("Description"));
        form.add(new JScrollPane(description));

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add College" : "Edit College",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        if (name.getText().trim().isEmpty() || location.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (existing == null) {
            DummyData.colleges.add(new College(DummyData.nextCollegeId(), name.getText().trim(),
                    location.getText().trim(), email.getText().trim(), description.getText().trim()));
            JOptionPane.showMessageDialog(this, "College added successfully.",
                    "Add College", JOptionPane.INFORMATION_MESSAGE);
        } else {
            existing.setCollegeName(name.getText().trim());
            existing.setLocation(location.getText().trim());
            existing.setEmail(email.getText().trim());
            existing.setDescription(description.getText().trim());
            JOptionPane.showMessageDialog(this, "College updated successfully.",
                    "Edit College", JOptionPane.INFORMATION_MESSAGE);
        }
        refresh();
    }
}
