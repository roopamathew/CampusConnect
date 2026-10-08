package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
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

public class ManageEventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageEventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.BG);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel("Manage Events");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);

        model = new DefaultTableModel(new String[] {"ID", "Event Name", "College", "Category", "Date", "Status"}, 0) {
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
        JButton add = AppTheme.primaryButton("+ ADD EVENT");
        JButton edit = AppTheme.secondaryButton("EDIT");
        JButton delete = AppTheme.dangerButton("DELETE");
        JButton view = AppTheme.accentButton("VIEW");
        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> editSelected());
        delete.addActionListener(e -> deleteSelected());
        view.addActionListener(e -> viewSelected());
        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(view);

        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        for (int i = 0; i < DummyData.events.size(); i++) {
            Event event = DummyData.events.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(event.getEventId()),
                    event.getEventName(),
                    event.getCollege(),
                    event.getCategory(),
                    event.getDate().toString(),
                    event.getStatus()
            });
        }
    }

    private Event selectedEvent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event first.",
                    "Manage Events", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = ((Integer) model.getValueAt(row, 0)).intValue();
        return DummyData.findEventById(id);
    }

    private void editSelected() {
        Event event = selectedEvent();
        if (event != null) {
            openForm(event);
        }
    }

    private void viewSelected() {
        Event event = selectedEvent();
        if (event != null) {
            mainFrame.showEventDetails(event, MainFrame.MANAGE_EVENTS);
        }
    }

    private void deleteSelected() {
        Event event = selectedEvent();
        if (event == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this event?",
                "Delete Event", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            DummyData.deleteEvent(event);
            refresh();
            JOptionPane.showMessageDialog(this, "Event deleted successfully.",
                    "Delete Event", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openForm(Event existing) {
        final JDialog dialog = new JDialog(mainFrame, existing == null ? "Add Event" : "Edit Event", true);
        dialog.setSize(520, 640);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.CARD);
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1;

        JTextField name = field(existing == null ? "" : existing.getEventName());
        JTextArea description = new JTextArea(existing == null ? "" : existing.getDescription(), 4, 20);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFont(AppTheme.BODY);
        JComboBox<String> category = new JComboBox<String>(DummyData.CATEGORIES);
        JComboBox<String> college = new JComboBox<String>(DummyData.getCollegeNames().toArray(new String[0]));
        JTextField venue = field(existing == null ? "" : existing.getVenue());
        JTextField location = field(existing == null ? "" : existing.getLocation());
        JTextField date = field(existing == null ? "2026-10-20" : existing.getDate().toString());
        JTextField start = field(existing == null ? "09:00" : existing.getStartTime().toString());
        JTextField end = field(existing == null ? "17:00" : existing.getEndTime().toString());
        JTextField deadline = field(existing == null ? "2026-10-15" : existing.getRegistrationDeadline().toString());
        JTextField contact = field(existing == null ? "" : existing.getContactPerson());
        JTextField email = field(existing == null ? "" : existing.getContactEmail());
        JTextField max = field(existing == null ? "100" : String.valueOf(existing.getMaxParticipants()));
        JComboBox<String> status = new JComboBox<String>(DummyData.STATUSES);

        if (existing != null) {
            category.setSelectedItem(existing.getCategory());
            college.setSelectedItem(existing.getCollege());
            status.setSelectedItem(existing.getStatus());
        }

        int row = 0;
        row = addField(form, gbc, row, "Event Name", name);
        gbc.gridy = row++;
        form.add(new JLabel("Description"), gbc);
        gbc.gridy = row++;
        form.add(new JScrollPane(description), gbc);
        row = addField(form, gbc, row, "Category", category);
        row = addField(form, gbc, row, "College", college);
        row = addField(form, gbc, row, "Venue", venue);
        row = addField(form, gbc, row, "Location", location);
        row = addField(form, gbc, row, "Date (YYYY-MM-DD)", date);
        row = addField(form, gbc, row, "Start Time (HH:MM)", start);
        row = addField(form, gbc, row, "End Time (HH:MM)", end);
        row = addField(form, gbc, row, "Registration Deadline", deadline);
        row = addField(form, gbc, row, "Contact Person", contact);
        row = addField(form, gbc, row, "Contact Email", email);
        row = addField(form, gbc, row, "Maximum Participants", max);
        row = addField(form, gbc, row, "Status", status);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton save = AppTheme.primaryButton("SAVE");
        JButton cancel = AppTheme.secondaryButton("CANCEL");
        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            if (isBlank(name) || isBlank(venue) || isBlank(location) || isBlank(contact) || isBlank(email)
                    || description.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                LocalDate eventDate = LocalDate.parse(date.getText().trim());
                LocalTime startTime = LocalTime.parse(start.getText().trim());
                LocalTime endTime = LocalTime.parse(end.getText().trim());
                LocalDate close = LocalDate.parse(deadline.getText().trim());
                int maxP = Integer.parseInt(max.getText().trim());
                if (maxP <= 0) {
                    throw new NumberFormatException("max");
                }
                if (existing == null) {
                    Event created = new Event(DummyData.nextEventId(), name.getText().trim(),
                            description.getText().trim(), String.valueOf(category.getSelectedItem()),
                            String.valueOf(college.getSelectedItem()), venue.getText().trim(),
                            location.getText().trim(), eventDate, startTime, endTime, close,
                            contact.getText().trim(), email.getText().trim(), maxP, maxP,
                            String.valueOf(status.getSelectedItem()));
                    DummyData.events.add(created);
                    JOptionPane.showMessageDialog(dialog, "Event added successfully.",
                            "Add Event", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    existing.setEventName(name.getText().trim());
                    existing.setDescription(description.getText().trim());
                    existing.setCategory(String.valueOf(category.getSelectedItem()));
                    existing.setCollege(String.valueOf(college.getSelectedItem()));
                    existing.setVenue(venue.getText().trim());
                    existing.setLocation(location.getText().trim());
                    existing.setDate(eventDate);
                    existing.setStartTime(startTime);
                    existing.setEndTime(endTime);
                    existing.setRegistrationDeadline(close);
                    existing.setContactPerson(contact.getText().trim());
                    existing.setContactEmail(email.getText().trim());
                    existing.setMaxParticipants(maxP);
                    existing.setStatus(String.valueOf(status.getSelectedItem()));
                    JOptionPane.showMessageDialog(dialog, "Event updated successfully.",
                            "Edit Event", JOptionPane.INFORMATION_MESSAGE);
                }
                refresh();
                dialog.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid date (YYYY-MM-DD) and time (HH:MM).",
                        "Invalid event information", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Maximum participants must be a positive number.",
                        "Invalid event information", JOptionPane.ERROR_MESSAGE);
            }
        });
        actions.add(save);
        actions.add(cancel);
        gbc.gridy = row;
        form.add(actions, gbc);

        dialog.setContentPane(new JScrollPane(form));
        dialog.setVisible(true);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridy = row++;
        form.add(new JLabel(label), gbc);
        gbc.gridy = row++;
        form.add(field, gbc);
        return row;
    }

    private JTextField field(String value) {
        JTextField field = new JTextField(value);
        field.setFont(AppTheme.BODY);
        return field;
    }

    private boolean isBlank(JTextField field) {
        return field.getText().trim().isEmpty();
    }
}
