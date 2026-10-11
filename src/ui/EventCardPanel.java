package ui;

import data.DummyData;
import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Reusable event card with modern rounded geometry, status badges,
 * metadata chips, creator attribution, and interactive quick-actions.
 */
public class EventCardPanel extends RoundedCard {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    public EventCardPanel(final Event event, final MainFrame mainFrame, final String backCard) {
        super(14, AppTheme.CARD, AppTheme.BORDER);
        setHoverable(true);
        setLayout(new BorderLayout(12, 10));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        setAlignmentX(LEFT_ALIGNMENT);

        final User currentUser = mainFrame.getCurrentUser();
        boolean isOtherCampus = currentUser != null
                && currentUser.getCollege() != null
                && !currentUser.getCollege().equalsIgnoreCase(event.getCollege());

        // TOP HEADER: Badges & Campus Indicator
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel badgesLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badgesLeft.setOpaque(false);
        badgesLeft.add(PillBadge.category(event.getCategory()));
        badgesLeft.add(PillBadge.status(event.getStatus()));

        if (isOtherCampus) {
            PillBadge beyondBadge = new PillBadge("BEYOND CAMPUS",
                    new Color(252, 246, 230), AppTheme.ACCENT.darker(), new Color(230, 205, 160));
            badgesLeft.add(beyondBadge);
        }
        topRow.add(badgesLeft, BorderLayout.WEST);

        JLabel seatsLabel = new JLabel(event.getAvailableSeats() > 0
                ? event.getAvailableSeats() + " seats left"
                : "Full capacity");
        seatsLabel.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        seatsLabel.setForeground(event.getAvailableSeats() > 0 ? AppTheme.SUCCESS : AppTheme.DANGER);
        topRow.add(seatsLabel, BorderLayout.EAST);

        add(topRow, BorderLayout.NORTH);

        // CENTER: Title & Details
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(event.getEventName());
        titleLabel.setFont(AppTheme.HEADING.deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(AppTheme.NAVY);
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(4));

        JLabel collegeLoc = new JLabel(event.getCollege() + "  ·  " + event.getVenue() + ", " + event.getLocation());
        collegeLoc.setFont(AppTheme.BODY);
        collegeLoc.setForeground(AppTheme.MUTED);
        center.add(collegeLoc);
        center.add(Box.createVerticalStrut(6));

        JPanel scheduleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        scheduleRow.setOpaque(false);

        JLabel dateLabel = new JLabel("📅  " + event.getDate().format(DATE_FMT));
        dateLabel.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        dateLabel.setForeground(AppTheme.TEXT);

        JLabel timeLabel = new JLabel("🕒  " + event.getStartTime().format(TIME_FMT) + " – " + event.getEndTime().format(TIME_FMT));
        timeLabel.setFont(AppTheme.SMALL);
        timeLabel.setForeground(AppTheme.MUTED);

        JLabel deadlineLabel = new JLabel("⏳  Reg. Deadline: " + event.getRegistrationDeadline().format(DATE_FMT));
        deadlineLabel.setFont(AppTheme.SMALL);
        deadlineLabel.setForeground(AppTheme.MUTED);

        scheduleRow.add(dateLabel);
        scheduleRow.add(timeLabel);
        scheduleRow.add(deadlineLabel);
        center.add(scheduleRow);

        add(center, BorderLayout.CENTER);

        // SOUTH: Actions & Creator attribution
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        // Creator Attribution snippet
        String creatorText = "Posted by: " + (event.getCreatorName() != null && !event.getCreatorName().isEmpty()
                ? event.getCreatorName() : "Campus Admin");
        JLabel creatorLabel = new JLabel(creatorText);
        creatorLabel.setFont(AppTheme.SMALL);
        creatorLabel.setForeground(AppTheme.MUTED);
        bottomRow.add(creatorLabel, BorderLayout.WEST);

        // Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        ModernButton viewBtn = new ModernButton("View Details", ModernButton.Variant.PRIMARY);
        viewBtn.setCornerRadius(6);
        viewBtn.addActionListener(e -> mainFrame.showEventDetails(event, backCard));
        actions.add(viewBtn);

        if (currentUser != null && "STUDENT".equals(currentUser.getRole())) {
            final boolean isReg = DummyData.isRegistered(currentUser.getEmail(), event.getEventId());
            final ModernButton regBtn = new ModernButton(isReg ? "Registered" : "Register",
                    isReg ? ModernButton.Variant.SECONDARY : ModernButton.Variant.SUCCESS);
            regBtn.setCornerRadius(6);
            if (isReg) {
                regBtn.setEnabled(false);
            } else {
                regBtn.addActionListener(e -> {
                    if (DummyData.isRegistered(currentUser.getEmail(), event.getEventId())) {
                        JOptionPane.showMessageDialog(EventCardPanel.this,
                                "You are already registered for this event.",
                                "Event Registration", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if (!"Registration Open".equalsIgnoreCase(event.getStatus()) && !"Upcoming".equalsIgnoreCase(event.getStatus())) {
                        JOptionPane.showMessageDialog(EventCardPanel.this,
                                "Registration is currently closed for this event.",
                                "Event Registration", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    boolean ok = DummyData.register(currentUser, event);
                    if (ok) {
                        JOptionPane.showMessageDialog(EventCardPanel.this,
                                "<html><b>Successfully registered!</b><br>You are registered for <b>" + event.getEventName() + "</b>.</html>",
                                "Registration Confirmed", JOptionPane.INFORMATION_MESSAGE);
                        regBtn.setText("Registered");
                        regBtn.setEnabled(false);
                        seatsLabel.setText(event.getAvailableSeats() > 0 ? event.getAvailableSeats() + " seats left" : "Full capacity");
                    }
                });
            }
            actions.add(regBtn);

            final boolean isBookmarked = DummyData.isBookmarked(currentUser.getEmail(), event.getEventId());
            final ModernButton bkmkBtn = new ModernButton(isBookmarked ? "★ Saved" : "☆ Bookmark",
                    isBookmarked ? ModernButton.Variant.ACCENT : ModernButton.Variant.SECONDARY);
            bkmkBtn.setCornerRadius(6);
            bkmkBtn.addActionListener(e -> {
                if (DummyData.isBookmarked(currentUser.getEmail(), event.getEventId())) {
                    DummyData.removeBookmark(currentUser.getEmail(), event.getEventId());
                    bkmkBtn.setText("☆ Bookmark");
                    bkmkBtn.setVariant(ModernButton.Variant.SECONDARY);
                } else {
                    DummyData.addBookmark(currentUser.getEmail(), event.getEventId());
                    bkmkBtn.setText("★ Saved");
                    bkmkBtn.setVariant(ModernButton.Variant.ACCENT);
                }
            });
            actions.add(bkmkBtn);
        }

        bottomRow.add(actions, BorderLayout.EAST);
        add(bottomRow, BorderLayout.SOUTH);
    }

    public static JPanel listOf(List<Event> events, MainFrame frame, String backCard) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(LEFT_ALIGNMENT);

        if (events == null || events.isEmpty()) {
            list.add(new EmptyState("🔍", "No Events Found",
                    "There are no events matching your criteria at this moment.",
                    "View All Events", () -> {
                if (frame != null) {
                    frame.showCard(MainFrame.EVENTS);
                }
            }));
        } else {
            for (int i = 0; i < events.size(); i++) {
                list.add(new EventCardPanel(events.get(i), frame, backCard));
                list.add(Box.createVerticalStrut(12));
            }
        }
        return list;
    }

    public static int upcomingCount() {
        return DummyData.countUpcomingEvents();
    }
}
