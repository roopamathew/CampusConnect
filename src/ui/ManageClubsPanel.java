package ui;

import data.DummyData;
import model.Club;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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

public class ManageClubsPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public ManageClubsPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.BG);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel("Manage Clubs");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);

        model = new DefaultTableModel(new String[] {"ID", "Club Name", "College", "Category"}, 0) {
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
            Club club = selected();
            if (club != null) {
                openForm(club);
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
        for (int i = 0; i < DummyData.clubs.size(); i++) {
            Club club = DummyData.clubs.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(club.getClubId()),
                    club.getClubName(),
                    club.getCollege(),
                    club.getCategory()
            });
        }
    }

    private Club selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a club first.",
                    "Manage Clubs", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        for (int i = 0; i < DummyData.clubs.size(); i++) {
            if (DummyData.clubs.get(i).getClubId() == id) {
                return DummyData.clubs.get(i);
            }
        }
        return null;
    }

    private void deleteSelected() {
        Club club = selected();
        if (club == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this club?",
                "Delete Club", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            DummyData.clubs.remove(club);
            refresh();
            JOptionPane.showMessageDialog(this, "Club deleted successfully.",
                    "Delete Club", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openForm(Club existing) {
        JTextField name = new JTextField(existing == null ? "" : existing.getClubName());
        JComboBox<String> college = new JComboBox<String>(DummyData.getCollegeNames().toArray(new String[0]));
        JComboBox<String> category = new JComboBox<String>(DummyData.CLUB_CATEGORIES);
        JTextArea description = new JTextArea(existing == null ? "" : existing.getDescription(), 3, 20);
        JTextField contact = new JTextField(existing == null ? "" : existing.getContactPerson());
        JTextField email = new JTextField(existing == null ? "" : existing.getContactEmail());
        if (existing != null) {
            college.setSelectedItem(existing.getCollege());
            category.setSelectedItem(existing.getCategory());
        }
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Club Name"));
        form.add(name);
        form.add(new JLabel("College"));
        form.add(college);
        form.add(new JLabel("Category"));
        form.add(category);
        form.add(new JLabel("Description"));
        form.add(new JScrollPane(description));
        form.add(new JLabel("Contact Person"));
        form.add(contact);
        form.add(new JLabel("Contact Email"));
        form.add(email);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add Club" : "Edit Club",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        if (name.getText().trim().isEmpty() || description.getText().trim().isEmpty()
                || contact.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (existing == null) {
            DummyData.clubs.add(new Club(DummyData.nextClubId(), name.getText().trim(),
                    String.valueOf(college.getSelectedItem()), String.valueOf(category.getSelectedItem()),
                    description.getText().trim(), contact.getText().trim(), email.getText().trim()));
            JOptionPane.showMessageDialog(this, "Club added successfully.",
                    "Add Club", JOptionPane.INFORMATION_MESSAGE);
        } else {
            existing.setClubName(name.getText().trim());
            existing.setCollege(String.valueOf(college.getSelectedItem()));
            existing.setCategory(String.valueOf(category.getSelectedItem()));
            existing.setDescription(description.getText().trim());
            existing.setContactPerson(contact.getText().trim());
            existing.setContactEmail(email.getText().trim());
            JOptionPane.showMessageDialog(this, "Club updated successfully.",
                    "Edit Club", JOptionPane.INFORMATION_MESSAGE);
        }
        refresh();
    }
}
