package ui;

import data.DummyData;
import model.Registration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminRegistrationsPanel extends JPanel {

    private final DefaultTableModel model;
    private final JComboBox<String> eventFilter = new JComboBox<String>();

    public AdminRegistrationsPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.BG);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel("Event Registrations");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterRow.setOpaque(false);
        filterRow.add(new JLabel("Filter By Event"));
        filterRow.add(eventFilter);
        top.add(heading, BorderLayout.NORTH);
        top.add(filterRow, BorderLayout.SOUTH);

        model = new DefaultTableModel(
                new String[] {"Student Name", "Student Email", "Event Name", "College", "Registration Date"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(AppTheme.BODY);
        table.getTableHeader().setFont(AppTheme.BOLD);

        eventFilter.addActionListener(e -> fillTable());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        Object selected = eventFilter.getSelectedItem();
        eventFilter.removeActionListener(eventFilter.getActionListeners()[0]);
        eventFilter.removeAllItems();
        eventFilter.addItem("All Events");
        for (int i = 0; i < DummyData.events.size(); i++) {
            eventFilter.addItem(DummyData.events.get(i).getEventName());
        }
        if (selected != null) {
            eventFilter.setSelectedItem(selected);
        }
        eventFilter.addActionListener(e -> fillTable());
        fillTable();
    }

    private void fillTable() {
        model.setRowCount(0);
        String filter = String.valueOf(eventFilter.getSelectedItem());
        for (int i = 0; i < DummyData.registrations.size(); i++) {
            Registration registration = DummyData.registrations.get(i);
            if ("All Events".equals(filter) || registration.getEventName().equals(filter)) {
                model.addRow(new Object[] {
                        registration.getStudentName(),
                        registration.getStudentEmail(),
                        registration.getEventName(),
                        registration.getCollege(),
                        registration.getRegistrationDate().toString()
                });
            }
        }
    }
}
