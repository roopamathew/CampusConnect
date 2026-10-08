package ui;

import data.DummyData;
import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EventDetailsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel inner;
    private String backCard = MainFrame.EVENTS;
    private Event currentEvent;

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMMM yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("h:mm a");

    public EventDetailsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        inner = UiUtil.page("Event Details", null);
        add(UiUtil.scroll(inner), BorderLayout.CENTER);
    }

    public void showEvent(Event event, String backCard) {
        this.currentEvent = event;
        this.backCard = backCard == null ? MainFrame.EVENTS : backCard;
        rebuild();
    }

    private void rebuild() {
        inner.removeAll();
        if (currentEvent == null) {
            inner.add(AppTheme.muted("Select an event to view details."));
            inner.revalidate();
            inner.repaint();
            return;
        }
        Event event = currentEvent;
        User user = mainFrame.getCurrentUser();

        JLabel name = new JLabel(event.getEventName().toUpperCase());
        name.setFont(AppTheme.TITLE);
        name.setForeground(AppTheme.NAVY);
        name.setAlignmentX(LEFT_ALIGNMENT);
        inner.add(name);
        inner.add(AppTheme.muted(event.getCollege() + "  ·  " + event.getCategory()));
        inner.add(Box.createVerticalStrut(16));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        AppTheme.styleCard(card);
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.add(html(event.getDescription()));
        card.add(Box.createVerticalStrut(10));
        card.add(row("Category", event.getCategory()));
        card.add(row("College", event.getCollege()));
        card.add(row("Venue", event.getVenue()));
        card.add(row("Location", event.getLocation()));
        card.add(row("Date", event.getDate().format(DATE)));
        card.add(row("Start Time", event.getStartTime().format(TIME)));
        card.add(row("End Time", event.getEndTime().format(TIME)));
        card.add(row("Registration Deadline", event.getRegistrationDeadline().format(DATE)));
        card.add(row("Contact Person", event.getContactPerson()));
        card.add(row("Contact Email", event.getContactEmail()));
        card.add(row("Maximum Participants", String.valueOf(event.getMaxParticipants())));
        card.add(row("Available Seats", String.valueOf(event.getAvailableSeats())));
        card.add(row("Status", event.getStatus()));
        inner.add(card);
        inner.add(Box.createVerticalStrut(16));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);

        if ("STUDENT".equals(user.getRole())) {
            final JButton register = AppTheme.primaryButton("REGISTER");
            final JButton bookmark = AppTheme.accentButton("BOOKMARK");
            if (DummyData.isRegistered(user.getEmail(), event.getEventId())) {
                register.setText("REGISTERED");
                register.setEnabled(false);
            }
            if (DummyData.isBookmarked(user.getEmail(), event.getEventId())) {
                bookmark.setText("BOOKMARKED");
            }
            register.addActionListener(e -> {
                if (DummyData.isRegistered(user.getEmail(), event.getEventId())) {
                    JOptionPane.showMessageDialog(this, "You are already registered for this event.",
                            "Registration", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (!"Registration Open".equals(event.getStatus()) && !"Upcoming".equals(event.getStatus())) {
                    JOptionPane.showMessageDialog(this, "Registration is not open for this event.",
                            "Registration", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                DummyData.register(user, event);
                JOptionPane.showMessageDialog(this, "Registration successful!",
                        "Registration", JOptionPane.INFORMATION_MESSAGE);
                register.setText("REGISTERED");
                register.setEnabled(false);
            });
            bookmark.addActionListener(e -> {
                if (DummyData.isBookmarked(user.getEmail(), event.getEventId())) {
                    JOptionPane.showMessageDialog(this, "Event already bookmarked.",
                            "Bookmark", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DummyData.addBookmark(user.getEmail(), event.getEventId());
                JOptionPane.showMessageDialog(this, "Event bookmarked!",
                        "Bookmark", JOptionPane.INFORMATION_MESSAGE);
                bookmark.setText("BOOKMARKED");
            });
            actions.add(register);
            actions.add(bookmark);
        }

        JButton back = AppTheme.secondaryButton("BACK");
        back.addActionListener(e -> mainFrame.showCard(backCard));
        actions.add(back);
        inner.add(actions);

        inner.revalidate();
        inner.repaint();
    }

    private JLabel html(String text) {
        JLabel label = new JLabel("<html><body style='width:700px'>" + text + "</body></html>");
        label.setFont(AppTheme.BODY);
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    private JLabel row(String label, String value) {
        JLabel line = new JLabel(label + ":  " + value);
        line.setFont(AppTheme.BODY);
        line.setForeground(AppTheme.TEXT);
        return line;
    }
}
