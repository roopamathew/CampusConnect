package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Reusable event card used on the dashboard, explore page and college events.
 */
public class EventCardPanel extends JPanel {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMMM yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("h:mm a");

    public EventCardPanel(final Event event, final MainFrame mainFrame, final String backCard) {
        setLayout(new BorderLayout(8, 8));
        AppTheme.styleCard(this);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        setAlignmentX(LEFT_ALIGNMENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel name = new JLabel(event.getEventName());
        name.setFont(AppTheme.HEADING);
        name.setForeground(AppTheme.NAVY);

        JLabel category = new JLabel(event.getCategory());
        category.setFont(AppTheme.BOLD);
        category.setForeground(AppTheme.TEAL);
        category.setHorizontalAlignment(SwingConstants.RIGHT);

        top.add(name, BorderLayout.CENTER);
        top.add(category, BorderLayout.EAST);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.add(AppTheme.muted("Organizing College: " + event.getCollege()));
        details.add(AppTheme.muted("Location: " + event.getLocation()));
        details.add(Box.createVerticalStrut(6));
        details.add(line("Date: " + event.getDate().format(DATE)));
        details.add(line("Time: " + event.getStartTime().format(TIME) + " – " + event.getEndTime().format(TIME)));
        details.add(Box.createVerticalStrut(6));
        details.add(AppTheme.muted("Registration Deadline: " + event.getRegistrationDeadline().format(DATE)));
        details.add(statusLabel(event.getStatus()));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        JButton view = AppTheme.primaryButton("VIEW DETAILS");
        view.addActionListener(e -> mainFrame.showEventDetails(event, backCard));
        actions.add(view);

        add(top, BorderLayout.NORTH);
        add(details, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private JLabel line(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.BODY);
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    private JLabel statusLabel(String status) {
        JLabel label = new JLabel("Status: " + status);
        label.setFont(AppTheme.BOLD);
        if ("Registration Open".equals(status) || "Upcoming".equals(status)) {
            label.setForeground(AppTheme.SUCCESS);
        } else if ("Completed".equals(status) || "Registration Closed".equals(status)) {
            label.setForeground(AppTheme.DANGER);
        } else {
            label.setForeground(AppTheme.TEAL);
        }
        return label;
    }

    public static JPanel listOf(java.util.List<Event> events, MainFrame frame, String backCard) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        if (events.isEmpty()) {
            list.add(AppTheme.muted("No events to display."));
        } else {
            for (int i = 0; i < events.size(); i++) {
                list.add(new EventCardPanel(events.get(i), frame, backCard));
                list.add(Box.createVerticalStrut(10));
            }
        }
        return list;
    }

    public static int upcomingCount() {
        return DummyData.countUpcomingEvents();
    }
}
