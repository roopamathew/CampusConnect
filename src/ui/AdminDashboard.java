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
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dedicated College Administrator Dashboard.
 * Scoped strictly to the administrator's assigned college.
 */
public class AdminDashboard extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel content;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMM yyyy");

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        add(UiUtil.scroll(content), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        content.removeAll();
        User admin = mainFrame.getCurrentUser();
        if (admin == null) return;

        String college = admin.getCollege();

        // 1. ADMIN WELCOME BANNER
        RoundedCard hero = new RoundedCard(14, AppTheme.NAVY, AppTheme.NAVY_DARK);
        hero.setLayout(new BorderLayout(16, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        InitialsAvatar avatar = new InitialsAvatar(admin.getFullName(), 54, AppTheme.INDIGO, Color.WHITE);
        hero.add(avatar, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);

        JLabel welcome = new JLabel("Welcome, " + admin.getFullName());
        welcome.setFont(AppTheme.TITLE.deriveFont(20f));
        welcome.setForeground(Color.WHITE);
        titleRow.add(welcome);

        PillBadge roleBadge = PillBadge.role(admin.getRole());
        titleRow.add(roleBadge);
        details.add(titleRow);
        details.add(Box.createVerticalStrut(4));

        String desig = (admin.getDesignation() != null && !admin.getDesignation().isEmpty())
                ? admin.getDesignation() : "College Administrator";
        JLabel scope = new JLabel("🏛  Assigned Campus: " + college + "   ·   " + desig);
        scope.setFont(AppTheme.BODY);
        scope.setForeground(AppTheme.LAVENDER);
        details.add(scope);

        hero.add(details, BorderLayout.CENTER);
        content.add(hero);
        content.add(Box.createVerticalStrut(20));

        // 2. LIVE SCOPED STATISTICS
        int collegeEvents = DummyData.getEventsByCollege(college).size();
        int collegeRegs = DummyData.getRegistrationsByCollege(college).size();
        int collegeClubs = DummyData.getClubsByCollege(college).size();
        int collegeNotices = DummyData.getNoticesByCollege(college).size();

        content.add(UiUtil.statsRow(
                UiUtil.modernStatCard("Managed Events", String.valueOf(collegeEvents), "Hosted at " + college, AppTheme.INDIGO),
                UiUtil.modernStatCard("Student Registrations", String.valueOf(collegeRegs), "Across hosted events", AppTheme.SUCCESS),
                UiUtil.modernStatCard("Campus Clubs", String.valueOf(collegeClubs), "Active communities", AppTheme.ACCENT),
                UiUtil.modernStatCard("College Notices", String.valueOf(collegeNotices), "Published bulletins", AppTheme.NAVY)
        ));
        content.add(Box.createVerticalStrut(20));

        // 3. QUICK MANAGEMENT ACTIONS
        RoundedCard actionsCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        actionsCard.setLayout(new BorderLayout(12, 0));
        actionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        actionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel actTitle = new JLabel("Campus Administration Shortcuts:");
        actTitle.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        actTitle.setForeground(AppTheme.NAVY);
        actionsCard.add(actTitle, BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        ModernButton addEventBtn = new ModernButton("+ Create Event", ModernButton.Variant.PRIMARY);
        addEventBtn.setCornerRadius(6);
        addEventBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_EVENTS));

        ModernButton addNoticeBtn = new ModernButton("+ Post Notice", ModernButton.Variant.SECONDARY);
        addNoticeBtn.setCornerRadius(6);
        addNoticeBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_NOTICES));

        ModernButton addClubBtn = new ModernButton("+ Add Club", ModernButton.Variant.SECONDARY);
        addClubBtn.setCornerRadius(6);
        addClubBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_CLUBS));

        ModernButton viewRegsBtn = new ModernButton("View Registrations", ModernButton.Variant.SECONDARY);
        viewRegsBtn.setCornerRadius(6);
        viewRegsBtn.addActionListener(e -> mainFrame.showCard(MainFrame.ADMIN_REGISTRATIONS));

        btnRow.add(addEventBtn);
        btnRow.add(addNoticeBtn);
        btnRow.add(addClubBtn);
        btnRow.add(viewRegsBtn);

        actionsCard.add(btnRow, BorderLayout.EAST);
        content.add(actionsCard);
        content.add(Box.createVerticalStrut(22));

        // 4. RECENT EVENTS TABLE / LIST FOR THIS COLLEGE
        content.add(UiUtil.sectionTitle("Events Hosted by " + college,
                "Events created and coordinated for your campus"));
        content.add(Box.createVerticalStrut(12));

        List<Event> myEvents = DummyData.getEventsByCollege(college);
        if (myEvents.isEmpty()) {
            content.add(new EmptyState("📅", "No Events Created Yet",
                    "You have not created any events for " + college + " yet.",
                    "+ Create First Event", () -> mainFrame.showCard(MainFrame.MANAGE_EVENTS)));
        } else {
            for (int i = 0; i < myEvents.size(); i++) {
                final Event ev = myEvents.get(i);
                RoundedCard evCard = new RoundedCard(10, AppTheme.CARD, AppTheme.BORDER);
                evCard.setLayout(new BorderLayout(14, 0));
                evCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
                evCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel mid = new JPanel();
                mid.setOpaque(false);
                mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

                JLabel title = new JLabel(ev.getEventName());
                title.setFont(AppTheme.HEADING.deriveFont(14f));
                title.setForeground(AppTheme.NAVY);
                mid.add(title);

                JLabel meta = new JLabel(ev.getCategory() + "   ·   📅 " + ev.getDate().format(DATE_FMT)
                        + "   ·   Venue: " + ev.getVenue() + "   ·   Seats Left: " + ev.getAvailableSeats() + " / " + ev.getMaxParticipants());
                meta.setFont(AppTheme.SMALL);
                meta.setForeground(AppTheme.MUTED);
                mid.add(meta);

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                right.setOpaque(false);
                right.add(PillBadge.status(ev.getStatus()));

                ModernButton manageBtn = new ModernButton("Manage", ModernButton.Variant.SECONDARY);
                manageBtn.setCornerRadius(6);
                manageBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_EVENTS));
                right.add(manageBtn);

                evCard.add(mid, BorderLayout.CENTER);
                evCard.add(right, BorderLayout.EAST);

                content.add(evCard);
                content.add(Box.createVerticalStrut(10));
            }
        }

        content.revalidate();
        content.repaint();
    }
}
