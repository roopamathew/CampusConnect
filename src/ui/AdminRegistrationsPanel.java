package ui;

import data.DummyData;
import model.Event;
import model.Registration;
import model.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Event registrations panel for administrators, scoped to their campus events.
 */
public class AdminRegistrationsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final AutoCompleteComboBox eventFilter = AutoCompleteComboBox.freeText(new String[]{"All Events"});
    private final JLabel countLabel = new JLabel();

    public AdminRegistrationsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        User currentUser = mainFrame.getCurrentUser();
        String subtitle = (currentUser != null && !"SUPER_ADMIN".equals(currentUser.getRole()))
                ? "Attendee registrations for events hosted by " + currentUser.getCollege()
                : "Platform-wide event registrations overview";

        JPanel header = UiUtil.pageHeader("Event Registrations", subtitle, null);

        // Filter Bar
        RoundedCard filterBar = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        filterBar.setLayout(new BorderLayout(12, 0));
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel filterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterLeft.setOpaque(false);
        JLabel filterLbl = new JLabel("Filter by Hosted Event:");
        filterLbl.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        filterLbl.setForeground(AppTheme.NAVY);
        filterLeft.add(filterLbl);

        AppTheme.styleComboBox(eventFilter);
        eventFilter.setPreferredSize(new Dimension(280, 34));
        filterLeft.add(eventFilter);
        filterBar.add(filterLeft, BorderLayout.WEST);

        countLabel.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        countLabel.setForeground(AppTheme.MUTED);
        JPanel filterRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterRight.setOpaque(false);
        filterRight.add(countLabel);
        filterBar.add(filterRight, BorderLayout.EAST);

        JPanel northWrapper = new JPanel();
        northWrapper.setOpaque(false);
        northWrapper.setLayout(new javax.swing.BoxLayout(northWrapper, javax.swing.BoxLayout.Y_AXIS));
        northWrapper.add(header);
        northWrapper.add(javax.swing.Box.createVerticalStrut(14));
        northWrapper.add(filterBar);
        add(northWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[] {"Student Name", "Student Email", "Event Name", "Host College", "Registration Date"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);

        JPanel tableWrapper = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        tableWrapper.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.CARD);
        tableWrapper.add(scroll, BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        eventFilter.addActionListener(e -> fillTable());
        refresh();
    }

    public void refresh() {
        User user = mainFrame.getCurrentUser();
        boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        String adminCollege = user != null ? user.getCollege() : "";

        String selected = eventFilter.getValue();
        List<String> items = new ArrayList<String>();
        items.add("All Events");

        for (Event ev : DummyData.events) {
            if (isSuper || (adminCollege != null && adminCollege.equalsIgnoreCase(ev.getCollege()))) {
                items.add(ev.getEventName());
            }
        }

        eventFilter.setItems(items.toArray(new String[0]));

        if (selected != null && !selected.isEmpty()) {
            eventFilter.setValue(selected);
        }
        fillTable();
    }

    private void fillTable() {
        model.setRowCount(0);
        User user = mainFrame.getCurrentUser();
        boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        String adminCollege = user != null ? user.getCollege() : "";

        String filter = eventFilter.getValue();
        int count = 0;

        for (Registration reg : DummyData.registrations) {
            boolean matchesCollege = isSuper || (adminCollege != null && adminCollege.equalsIgnoreCase(reg.getCollege()));
            boolean matchesEvent = "All Events".equals(filter) || reg.getEventName().equals(filter);

            if (matchesCollege && matchesEvent) {
                model.addRow(new Object[] {
                        reg.getStudentName(),
                        reg.getStudentEmail(),
                        reg.getEventName(),
                        reg.getCollege(),
                        reg.getRegistrationDate().toString()
                });
                count++;
            }
        }

        countLabel.setText("Total Attendees: " + count);
    }
}
