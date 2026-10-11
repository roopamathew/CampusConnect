package ui;

import data.DummyData;
import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
 * Event management panel with strict college-scoping, automated creator attribution,
 * and comprehensive form validations.
 */
public class ManageEventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final DefaultTableModel model;
    private final JTable table;

    public ManageEventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBackground(AppTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        User currentUser = mainFrame.getCurrentUser();
        String subtitle = (currentUser != null && !"SUPER_ADMIN".equals(currentUser.getRole()))
                ? "Manage and coordinate events hosted by " + currentUser.getCollege()
                : "Platform-wide event oversight and management";

        JPanel header = UiUtil.pageHeader("Manage Campus Events", subtitle, null);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {"ID", "Event Name", "College", "Category", "Date", "Seats", "Status"}, 0) {
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

        ModernButton addBtn = new ModernButton("+ Add Event", ModernButton.Variant.PRIMARY);
        ModernButton editBtn = new ModernButton("✎ Edit Selected", ModernButton.Variant.SECONDARY);
        ModernButton deleteBtn = new ModernButton("✕ Delete", ModernButton.Variant.DANGER);
        ModernButton viewBtn = new ModernButton("View Details", ModernButton.Variant.SECONDARY);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
        viewBtn.addActionListener(e -> viewSelected());

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        buttons.add(viewBtn);
        add(buttons, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        User user = mainFrame.getCurrentUser();
        boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        String adminCollege = user != null ? user.getCollege() : "";

        List<Event> eventsToDisplay = new ArrayList<Event>();
        for (Event event : DummyData.events) {
            if (isSuper || (adminCollege != null && adminCollege.equalsIgnoreCase(event.getCollege()))) {
                eventsToDisplay.add(event);
            }
        }

        for (int i = 0; i < eventsToDisplay.size(); i++) {
            Event event = eventsToDisplay.get(i);
            model.addRow(new Object[] {
                    Integer.valueOf(event.getEventId()),
                    event.getEventName(),
                    event.getCollege(),
                    event.getCategory(),
                    event.getDate().toString(),
                    event.getAvailableSeats() + " / " + event.getMaxParticipants(),
                    event.getStatus()
            });
        }
    }

    private Event selectedEvent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event row first.",
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
        if (event == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "<html>Are you sure you want to delete event: <b>" + event.getEventName() + "</b>?<br>"
                        + "This will also cancel associated student registrations.</html>",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            DummyData.deleteEvent(event);
            refresh();
            JOptionPane.showMessageDialog(this, "Event deleted successfully.",
                    "Delete Event", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void openForm(Event existing) {
        final User user = mainFrame.getCurrentUser();
        final boolean isSuper = user != null && "SUPER_ADMIN".equals(user.getRole());
        final String adminCollege = user != null ? user.getCollege() : "Mar Baselios College";

        final JDialog dialog = new JDialog(mainFrame, existing == null ? "Create New Campus Event" : "Edit Campus Event", true);
        dialog.setSize(580, 680);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // Header
        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(AppTheme.NAVY);
        head.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel(existing == null ? "Add Event" : "Edit Event: " + existing.getEventName());
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

        JTextField name = field(existing == null ? "" : existing.getEventName());
        JTextArea description = new JTextArea(existing == null ? "" : existing.getDescription(), 4, 20);
        AppTheme.styleTextArea(description);

        AutoCompleteComboBox category = AutoCompleteComboBox.freeText(DummyData.CATEGORIES);

        // Institutional college assignment: derived from administrator to enforce boundaries
        AutoCompleteComboBox collegeBox = AutoCompleteComboBox.freeText(DummyData.getCollegeNames().toArray(new String[0]));
        JTextField lockedCollege = field(adminCollege);
        lockedCollege.setEditable(false);

        JTextField venue = field(existing == null ? "" : existing.getVenue());
        JTextField location = field(existing == null ? "" : existing.getLocation());
        JTextField date = field(existing == null ? LocalDate.now().plusDays(10).toString() : existing.getDate().toString());
        JTextField start = field(existing == null ? "09:30" : existing.getStartTime().toString());
        JTextField end = field(existing == null ? "16:30" : existing.getEndTime().toString());
        JTextField deadline = field(existing == null ? LocalDate.now().plusDays(7).toString() : existing.getRegistrationDeadline().toString());
        JTextField contact = field(existing == null ? (user != null ? user.getFullName() : "") : existing.getContactPerson());
        JTextField email = field(existing == null ? (user != null ? user.getEmail() : "") : existing.getContactEmail());
        JTextField max = field(existing == null ? "100" : String.valueOf(existing.getMaxParticipants()));
        AutoCompleteComboBox status = AutoCompleteComboBox.strict(DummyData.STATUSES);

        final ImagePickerField posterPicker = new ImagePickerField(existing != null ? existing.getImagePath() : "");

        if (existing != null) {
            category.setValue(existing.getCategory());
            collegeBox.setValue(existing.getCollege());
            status.setValue(existing.getStatus());
        }

        int row = 0;
        row = addField(form, gbc, row, "Event Title *", name);
        gbc.gridy = row++;
        form.add(new JLabel("Description *"), gbc);
        gbc.gridy = row++;
        form.add(new JScrollPane(description), gbc);

        row = addField(form, gbc, row, "Category *", category);

        // Enforce ownership: Non-superadmin is locked to assigned college
        if (isSuper) {
            row = addField(form, gbc, row, "Host College *", collegeBox);
        } else {
            row = addField(form, gbc, row, "Host College (Derived from Logged-In Administrator)", lockedCollege);
        }

        row = addField(form, gbc, row, "Venue * (e.g. Auditorium 2)", venue);
        row = addField(form, gbc, row, "City / Campus Location *", location);
        row = addField(form, gbc, row, "Event Date (YYYY-MM-DD) *", date);
        row = addField(form, gbc, row, "Start Time (HH:MM) *", start);
        row = addField(form, gbc, row, "End Time (HH:MM) *", end);
        row = addField(form, gbc, row, "Registration Deadline (YYYY-MM-DD) *", deadline);
        row = addField(form, gbc, row, "Event Coordinator Name *", contact);
        row = addField(form, gbc, row, "Contact Email *", email);
        row = addField(form, gbc, row, "Maximum Participant Capacity *", max);
        row = addField(form, gbc, row, "Status *", status);
        row = addField(form, gbc, row, "Event Poster Image (Optional)", posterPicker);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actions.setBackground(AppTheme.CARD);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Variant.SECONDARY);
        cancelBtn.addActionListener(e -> dialog.dispose());

        ModernButton saveBtn = new ModernButton("Save Event", ModernButton.Variant.PRIMARY);
        saveBtn.addActionListener(e -> {
            if (isBlank(name) || isBlank(venue) || isBlank(location) || isBlank(contact) || isBlank(email)
                    || description.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                LocalDate eventDate = LocalDate.parse(date.getText().trim());
                LocalTime startTime = LocalTime.parse(start.getText().trim());
                LocalTime endTime = LocalTime.parse(end.getText().trim());
                LocalDate closeDate = LocalDate.parse(deadline.getText().trim());
                int maxP = Integer.parseInt(max.getText().trim());

                if (maxP <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Maximum participant capacity must be greater than zero.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String chosenCollege = isSuper ? collegeBox.getValue() : adminCollege;

                // Derive creator attribution from authenticated administrator
                String creatorName = (user != null) ? user.getFullName() : contact.getText().trim();
                String creatorEmail = (user != null) ? user.getEmail() : email.getText().trim();
                String creatorDesig = (user != null && !user.getDesignation().isEmpty()) ? user.getDesignation() : "Event Coordinator";

                // Validate status against allowed values
                String chosenStatus = status.getValue();
                boolean validStatus = false;
                for (String s : DummyData.STATUSES) {
                    if (s.equalsIgnoreCase(chosenStatus)) { validStatus = true; chosenStatus = s; break; }
                }
                if (!validStatus) chosenStatus = DummyData.STATUSES[0];

                // Validate category
                String chosenCategory = category.getValue().isEmpty() ? DummyData.CATEGORIES[0] : category.getValue();

                if (existing == null) {
                    Event created = new Event(DummyData.nextEventId(), name.getText().trim(),
                            description.getText().trim(), chosenCategory,
                            chosenCollege, venue.getText().trim(), location.getText().trim(),
                            eventDate, startTime, endTime, closeDate,
                            contact.getText().trim(), email.getText().trim(), maxP, maxP,
                            chosenStatus,
                            creatorName, creatorEmail, creatorDesig);
                    created.setImagePath(posterPicker.getImagePath());
                    DummyData.events.add(created);

                    JOptionPane.showMessageDialog(dialog, "Event created successfully!",
                            "Add Event", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    existing.setEventName(name.getText().trim());
                    existing.setDescription(description.getText().trim());
                    existing.setCategory(chosenCategory);
                    if (isSuper) {
                        existing.setCollege(chosenCollege);
                    }
                    existing.setVenue(venue.getText().trim());
                    existing.setLocation(location.getText().trim());
                    existing.setDate(eventDate);
                    existing.setStartTime(startTime);
                    existing.setEndTime(endTime);
                    existing.setRegistrationDeadline(closeDate);
                    existing.setContactPerson(contact.getText().trim());
                    existing.setContactEmail(email.getText().trim());
                    existing.setMaxParticipants(maxP);
                    existing.setStatus(chosenStatus);
                    existing.setImagePath(posterPicker.getImagePath());

                    // Preserve existing creator attribution if already set
                    if (existing.getCreatorName() == null || existing.getCreatorName().isEmpty()) {
                        existing.setCreatorName(creatorName);
                        existing.setCreatorEmail(creatorEmail);
                        existing.setCreatorDesignation(creatorDesig);
                    }

                    JOptionPane.showMessageDialog(dialog, "Event updated successfully!",
                            "Edit Event", JOptionPane.INFORMATION_MESSAGE);
                }

                refresh();
                dialog.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date/time format. Use YYYY-MM-DD for dates and HH:MM for times.",
                        "Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Capacity must be a valid positive integer.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
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

    private JTextField field(String value) {
        JTextField field = new JTextField(value);
        AppTheme.styleField(field);
        return field;
    }

    private boolean isBlank(JTextField field) {
        return field.getText().trim().isEmpty();
    }
}
