package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
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
 * Super Admin panel for managing partner colleges and institutional directory.
 */
public class ManageCollegesPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageCollegesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel header = UiUtil.pageHeader("Manage Partner Colleges",
                "Directory of accredited institutions participating in CampusConnect", null);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {"ID", "College Name", "Location", "Official Email", "Description"}, 0) {
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

        ModernButton addBtn = new ModernButton("+ Add College", ModernButton.Variant.PRIMARY);
        ModernButton editBtn = new ModernButton("✎ Edit Selected", ModernButton.Variant.SECONDARY);
        ModernButton deleteBtn = new ModernButton("✕ Delete", ModernButton.Variant.DANGER);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            College college = selected();
            if (college != null) openForm(college);
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
        for (int i = 0; i < DummyData.colleges.size(); i++) {
            College c = DummyData.colleges.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(c.getCollegeId()),
                    c.getCollegeName(),
                    c.getLocation(),
                    c.getEmail(),
                    c.getDescription()
            });
        }
    }

    private College selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a college row first.",
                    "Manage Colleges", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (College c : DummyData.colleges) {
            if (c.getCollegeId() == id) {
                return c;
            }
        }
        return null;
    }

    private void deleteSelected() {
        College college = selected();
        if (college == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "<html>Are you sure you want to delete college: <b>" + college.getCollegeName() + "</b>?<br>"
                        + "This may affect associated events and notices.</html>",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            DummyData.colleges.remove(college);
            refresh();
            JOptionPane.showMessageDialog(this, "College removed successfully.",
                    "Delete College", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void openForm(College existing) {
        final JDialog dialog = new JDialog(mainFrame, existing == null ? "Add Partner College" : "Edit College", true);
        dialog.setSize(520, 500);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(AppTheme.NAVY);
        head.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel(existing == null ? "Register New College" : "Edit College");
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

        JTextField name = new JTextField(existing == null ? "" : existing.getCollegeName());
        AppTheme.styleField(name);

        JTextField location = new JTextField(existing == null ? "" : existing.getLocation());
        AppTheme.styleField(location);

        JTextField email = new JTextField(existing == null ? "" : existing.getEmail());
        AppTheme.styleField(email);

        JTextArea desc = new JTextArea(existing == null ? "" : existing.getDescription(), 3, 20);
        AppTheme.styleTextArea(desc);

        int row = 0;
        row = addField(form, gbc, row, "College / Institution Name *", name);
        row = addField(form, gbc, row, "Campus Location (City, State) *", location);
        row = addField(form, gbc, row, "Official Contact Email *", email);

        gbc.gridy = row++;
        JLabel descLabel = new JLabel("Campus Overview & Description *");
        descLabel.setFont(AppTheme.BOLD);
        descLabel.setForeground(AppTheme.NAVY);
        form.add(descLabel, gbc);

        gbc.gridy = row++;
        form.add(new JScrollPane(desc), gbc);

        root.add(new JScrollPane(form), BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actions.setBackground(AppTheme.CARD);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
        cancelBtn.addActionListener(e -> dialog.dispose());

        ModernButton saveBtn = new ModernButton("Save College", ModernButton.Variant.PRIMARY);
        saveBtn.addActionListener(e -> {
            String n = name.getText().trim();
            String l = location.getText().trim();
            String em = email.getText().trim();
            String d = desc.getText().trim();

            if (n.isEmpty() || l.isEmpty() || em.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (existing == null) {
                DummyData.colleges.add(new College(DummyData.nextCollegeId(), n, l, em, d));
                JOptionPane.showMessageDialog(dialog, "College registered successfully!", "College Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                existing.setCollegeName(n);
                existing.setLocation(l);
                existing.setEmail(em);
                existing.setDescription(d);
                JOptionPane.showMessageDialog(dialog, "College details updated successfully!", "College Updated", JOptionPane.INFORMATION_MESSAGE);
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
