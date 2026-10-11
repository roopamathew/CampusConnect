package ui;

import data.DummyData;
import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Dedicated event details screen with event banner placeholder, full schedule,
 * capacity meters, verified administrator creator attribution, and registration controls.
 */
public class EventDetailsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel inner;
    private String backCard = MainFrame.EVENTS;
    private Event currentEvent;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    public EventDetailsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(AppTheme.BG);
        inner.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

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
            inner.add(new EmptyState("🔍", "No Event Selected",
                    "Please select an event from the Explore Events page.",
                    "Explore Events", () -> mainFrame.showCard(MainFrame.EVENTS)));
            inner.revalidate();
            inner.repaint();
            return;
        }

        final Event event = currentEvent;
        final User user = mainFrame.getCurrentUser();

        // 1. BACK BUTTON BAR
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setOpaque(false);
        navBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        ModernButton backBtn = new ModernButton("← Back", ModernButton.Variant.SECONDARY);
        backBtn.setCornerRadius(6);
        backBtn.addActionListener(e -> mainFrame.showCard(backCard));
        navBar.add(backBtn);
        inner.add(navBar);
        inner.add(Box.createVerticalStrut(14));

        // 2. CONDITIONAL EVENT POSTER IMAGE (Only if uploaded and valid)
        if (event.hasImage()) {
            javax.swing.JComponent posterImg = ImageUtil.createImageViewer(event.getImagePath(), Integer.MAX_VALUE, 260, 14);
            if (posterImg != null) {
                inner.add(posterImg);
                inner.add(Box.createVerticalStrut(16));
            }
        }

        // HEADER CARD: Event Title, Host College and Category Badges
        RoundedCard headerCard = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        headerCard.setLayout(new BorderLayout(16, 12));
        headerCard.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        headerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel heroTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        heroTop.setOpaque(false);
        heroTop.add(PillBadge.category(event.getCategory()));
        heroTop.add(PillBadge.status(event.getStatus()));

        if (user != null && user.getCollege() != null && !user.getCollege().equalsIgnoreCase(event.getCollege())) {
            PillBadge beyondPill = new PillBadge("DISCOVER BEYOND YOUR CAMPUS",
                    new Color(250, 242, 222), AppTheme.ACCENT.darker());
            heroTop.add(beyondPill);
        }
        headerCard.add(heroTop, BorderLayout.NORTH);

        JPanel heroMid = new JPanel();
        heroMid.setOpaque(false);
        heroMid.setLayout(new BoxLayout(heroMid, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(event.getEventName());
        titleLbl.setFont(AppTheme.BRAND.deriveFont(22f));
        titleLbl.setForeground(AppTheme.TEXT);
        heroMid.add(titleLbl);
        heroMid.add(Box.createVerticalStrut(4));

        JLabel campusSubtitle = new JLabel("Organized by " + event.getCollege() + "  ·  " + event.getLocation());
        campusSubtitle.setFont(AppTheme.BODY);
        campusSubtitle.setForeground(AppTheme.MUTED);
        heroMid.add(campusSubtitle);

        headerCard.add(heroMid, BorderLayout.CENTER);
        inner.add(headerCard);
        inner.add(Box.createVerticalStrut(18));

        // 3. TWO-COLUMN LAYOUT: Left (Details & Schedule) & Right (Creator & Actions)
        JPanel columns = new JPanel(new BorderLayout(20, 0));
        columns.setOpaque(false);
        columns.setAlignmentX(Component.LEFT_ALIGNMENT);

        // LEFT COLUMN
        JPanel leftCol = new JPanel();
        leftCol.setOpaque(false);
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));

        // Description Card
        RoundedCard descCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        descCard.setLayout(new BoxLayout(descCard, BoxLayout.Y_AXIS));
        descCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descTitle = new JLabel("About this Event");
        descTitle.setFont(AppTheme.HEADING);
        descTitle.setForeground(AppTheme.NAVY);
        descCard.add(descTitle);
        descCard.add(Box.createVerticalStrut(10));

        JLabel descBody = new JLabel("<html><div style='width:520px;line-height:1.5;color:#1A233A;'>"
                + event.getDescription() + "</div></html>");
        descBody.setFont(AppTheme.BODY);
        descCard.add(descBody);
        leftCol.add(descCard);
        leftCol.add(Box.createVerticalStrut(16));

        // Schedule & Venue Card
        RoundedCard schedCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        schedCard.setLayout(new GridLayout(0, 2, 16, 12));
        schedCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        schedCard.add(detailItem("Date", event.getDate().format(DATE_FMT)));
        schedCard.add(detailItem("Time", event.getStartTime().format(TIME_FMT) + " – " + event.getEndTime().format(TIME_FMT)));
        schedCard.add(detailItem("Venue", event.getVenue()));
        schedCard.add(detailItem("Campus Location", event.getLocation()));
        schedCard.add(detailItem("Category", event.getCategory()));
        schedCard.add(detailItem("Registration Deadline", event.getRegistrationDeadline().format(DATE_FMT)));

        leftCol.add(schedCard);
        leftCol.add(Box.createVerticalStrut(16));

        // Capacity Card
        RoundedCard capCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        capCard.setLayout(new GridLayout(1, 3, 16, 0));
        capCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        capCard.add(detailItem("Max Participants", String.valueOf(event.getMaxParticipants())));
        capCard.add(detailItem("Seats Remaining", String.valueOf(event.getAvailableSeats())));
        capCard.add(detailItem("Status", event.getStatus()));

        leftCol.add(capCard);
        columns.add(leftCol, BorderLayout.CENTER);

        // RIGHT COLUMN
        JPanel rightCol = new JPanel();
        rightCol.setOpaque(false);
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setPreferredSize(new Dimension(340, 0));

        // 4. "POSTED BY" ADMINISTRATOR ATTRIBUTION CARD
        RoundedCard creatorCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        creatorCard.setLayout(new BoxLayout(creatorCard, BoxLayout.Y_AXIS));
        creatorCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel postedTitle = new JLabel("Event Organizer Attribution");
        postedTitle.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        postedTitle.setForeground(AppTheme.MUTED);
        creatorCard.add(postedTitle);
        creatorCard.add(Box.createVerticalStrut(12));

        String creatorName = (event.getCreatorName() != null && !event.getCreatorName().isEmpty())
                ? event.getCreatorName() : "Campus Administrator";
        String creatorEmail = (event.getCreatorEmail() != null && !event.getCreatorEmail().isEmpty())
                ? event.getCreatorEmail() : "admin@campusconnect.edu";
        String creatorDesig = (event.getCreatorDesignation() != null && !event.getCreatorDesignation().isEmpty())
                ? event.getCreatorDesignation() : "College Administrator";

        JPanel creatorRow = new JPanel(new BorderLayout(12, 0));
        creatorRow.setOpaque(false);

        InitialsAvatar creatorAvatar = new InitialsAvatar(creatorName, 44, AppTheme.INDIGO, Color.WHITE);
        creatorRow.add(creatorAvatar, BorderLayout.WEST);

        JPanel creatorDetails = new JPanel();
        creatorDetails.setOpaque(false);
        creatorDetails.setLayout(new BoxLayout(creatorDetails, BoxLayout.Y_AXIS));

        JLabel cName = new JLabel(creatorName);
        cName.setFont(AppTheme.HEADING.deriveFont(14f));
        cName.setForeground(AppTheme.NAVY);

        JLabel cDesig = new JLabel(creatorDesig);
        cDesig.setFont(AppTheme.SMALL);
        cDesig.setForeground(AppTheme.MUTED);

        JLabel cCollege = new JLabel(event.getCollege());
        cCollege.setFont(AppTheme.SMALL);
        cCollege.setForeground(AppTheme.INDIGO);

        creatorDetails.add(cName);
        creatorDetails.add(Box.createVerticalStrut(2));
        creatorDetails.add(cDesig);
        creatorDetails.add(Box.createVerticalStrut(2));
        creatorDetails.add(cCollege);

        creatorRow.add(creatorDetails, BorderLayout.CENTER);
        creatorCard.add(creatorRow);
        creatorCard.add(Box.createVerticalStrut(12));

        creatorCard.add(detailItem("Official Contact Email", creatorEmail));
        creatorCard.add(Box.createVerticalStrut(6));
        creatorCard.add(new PillBadge("VERIFIED INSTITUTIONAL POST",
                new Color(236, 248, 240), AppTheme.SUCCESS, new Color(190, 230, 205)));

        rightCol.add(creatorCard);
        rightCol.add(Box.createVerticalStrut(16));

        // 5. REGISTRATION ACTIONS CARD
        if (user != null && "STUDENT".equals(user.getRole())) {
            RoundedCard actionCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
            actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));
            actionCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel actHead = new JLabel("Your Registration Status");
            actHead.setFont(AppTheme.HEADING.deriveFont(14f));
            actHead.setForeground(AppTheme.NAVY);
            actionCard.add(actHead);
            actionCard.add(Box.createVerticalStrut(10));

            final boolean isReg = DummyData.isRegistered(user.getEmail(), event.getEventId());
            final boolean isBkmk = DummyData.isBookmarked(user.getEmail(), event.getEventId());

            if (isReg) {
                JLabel regDone = new JLabel("✓ You are registered for this event.");
                regDone.setFont(AppTheme.BOLD);
                regDone.setForeground(AppTheme.SUCCESS);
                actionCard.add(regDone);
            } else {
                JLabel regOpen = new JLabel("Seats remaining: " + event.getAvailableSeats());
                regOpen.setFont(AppTheme.SMALL);
                regOpen.setForeground(AppTheme.MUTED);
                actionCard.add(regOpen);
            }
            actionCard.add(Box.createVerticalStrut(12));

            ModernButton regButton = new ModernButton(isReg ? "Already Registered" : "Register For Event",
                    isReg ? ModernButton.Variant.SECONDARY : ModernButton.Variant.SUCCESS);
            regButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            regButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (isReg) {
                regButton.setEnabled(false);
            } else {
                regButton.addActionListener(e -> {
                    if (DummyData.isRegistered(user.getEmail(), event.getEventId())) {
                        JOptionPane.showMessageDialog(EventDetailsPanel.this,
                                "You are already registered for this event.",
                                "Registration", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if (!"Registration Open".equalsIgnoreCase(event.getStatus()) && !"Upcoming".equalsIgnoreCase(event.getStatus())) {
                        JOptionPane.showMessageDialog(EventDetailsPanel.this,
                                "Registration is not currently open.",
                                "Registration", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    boolean success = DummyData.register(user, event);
                    if (success) {
                        JOptionPane.showMessageDialog(EventDetailsPanel.this,
                                "<html><b>Registration Successful!</b><br>You are registered for <b>" + event.getEventName() + "</b>.</html>",
                                "Registration Confirmed", JOptionPane.INFORMATION_MESSAGE);
                        rebuild();
                    }
                });
            }
            actionCard.add(regButton);
            actionCard.add(Box.createVerticalStrut(8));

            ModernButton bkmkButton = new ModernButton(isBkmk ? "★ Bookmarked (Remove)" : "☆ Bookmark Event",
                    isBkmk ? ModernButton.Variant.ACCENT : ModernButton.Variant.SECONDARY);
            bkmkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            bkmkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            bkmkButton.addActionListener(e -> {
                if (DummyData.isBookmarked(user.getEmail(), event.getEventId())) {
                    DummyData.removeBookmark(user.getEmail(), event.getEventId());
                    JOptionPane.showMessageDialog(EventDetailsPanel.this, "Bookmark removed.", "Bookmarks", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    DummyData.addBookmark(user.getEmail(), event.getEventId());
                    JOptionPane.showMessageDialog(EventDetailsPanel.this, "Event saved to bookmarks!", "Bookmarks", JOptionPane.INFORMATION_MESSAGE);
                }
                rebuild();
            });
            actionCard.add(bkmkButton);

            rightCol.add(actionCard);
        }

        columns.add(rightCol, BorderLayout.EAST);
        inner.add(columns);

        inner.revalidate();
        inner.repaint();
    }

    private JPanel detailItem(String label, String value) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        lbl.setForeground(AppTheme.MUTED);

        JLabel val = new JLabel(value);
        val.setFont(AppTheme.BODY.deriveFont(Font.BOLD));
        val.setForeground(AppTheme.NAVY);

        p.add(lbl);
        p.add(Box.createVerticalStrut(2));
        p.add(val);
        return p;
    }
}
