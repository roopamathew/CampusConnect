package ui;

import data.DummyData;
import model.Event;
import model.Notice;
import model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

/**
 * Redesigned Student Discovery Dashboard.
 * Serves as the primary discovery hub for events, colleges, clubs, and notices.
 */
public class StudentDashboard extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel content;
    private final JTextField searchField = new JTextField();
    private static final String SEARCH_HINT = "Search events, categories, or colleges...";
    private boolean showingSearchHint = true;

    public StudentDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setScrollMode(javax.swing.JViewport.SIMPLE_SCROLL_MODE);

        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        content.removeAll();
        User user = mainFrame.getCurrentUser();
        if (user == null) {
            return;
        }

        // 1. WELCOME HERO CARD
        content.add(buildWelcomeHero(user));
        content.add(Box.createVerticalStrut(20));

        // 2. QUICK ACCESS ACTION PILLS
        content.add(buildQuickActionsBar());
        content.add(Box.createVerticalStrut(18));

        // 3. SUMMARY STATS ROW
        int upcoming = DummyData.countUpcomingEvents();
        int regs = DummyData.getRegistrationsForEmail(user.getEmail()).size();
        int bookmarks = DummyData.getBookmarkIds(user.getEmail()).size();
        int noticeCount = DummyData.notices.size();

        content.add(UiUtil.statsRow(
                UiUtil.modernStatCard("Upcoming Events", String.valueOf(upcoming), "Across all colleges", AppTheme.INDIGO),
                UiUtil.modernStatCard("My Registrations", String.valueOf(regs), "Active participation", AppTheme.SUCCESS),
                UiUtil.modernStatCard("My Bookmarks", String.valueOf(bookmarks), "Saved opportunities", AppTheme.ACCENT),
                UiUtil.modernStatCard("Active Notices", String.valueOf(noticeCount), "Latest broadcasts", AppTheme.NAVY)
        ));
        content.add(Box.createVerticalStrut(22));

        // 4. DISCOVER SEARCH BAR
        content.add(buildSearchBar());
        content.add(Box.createVerticalStrut(24));

        // 5. "DISCOVER BEYOND YOUR CAMPUS" - HIGH PRIORITY SECTION
        List<Event> beyondEvents = DummyData.getEventsBeyondCampus(user.getCollege());
        JPanel beyondBody = EventCardPanel.listOf(take(beyondEvents, 3), mainFrame, MainFrame.STUDENT_DASHBOARD);
        addHighlightSection("DISCOVER BEYOND YOUR CAMPUS",
                "Explore opportunities happening at partner colleges outside " + user.getCollege() + ".",
                beyondBody, () -> mainFrame.showCard(MainFrame.EVENTS));
        content.add(Box.createVerticalStrut(22));

        // 6. RECOMMENDED FOR YOU SECTION
        List<Event> recommended = DummyData.getRecommendedEvents(user.getInterest());
        addSection("Recommended For You (" + (user.getInterest() != null ? user.getInterest() : "All") + ")",
                "Curated events matching your primary interest area",
                EventCardPanel.listOf(take(recommended, 3), mainFrame, MainFrame.STUDENT_DASHBOARD));
        content.add(Box.createVerticalStrut(22));

        // 7. UPCOMING IN NEXT 7 DAYS TIMELINE
        addSection("Upcoming Soon (Next 7 Days)",
                "Events happening right around the corner",
                buildUpcomingSoonPanel());
        content.add(Box.createVerticalStrut(22));

        // 8. IMPORTANT NOTICES PREVIEW
        addSection("Campus Announcements",
                "Official broadcasts from college administrations",
                buildNoticesPreview());

        content.revalidate();
        content.repaint();
    }

    private JPanel buildWelcomeHero(User user) {
        RoundedCard hero = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        hero.setLayout(new BorderLayout(16, 0));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nameRow.setOpaque(false);

        JLabel welcome = new JLabel("Welcome back, " + user.getFullName() + "!");
        welcome.setFont(AppTheme.TITLE.deriveFont(22f));
        welcome.setForeground(AppTheme.TEXT);
        nameRow.add(welcome);

        PillBadge homeBadge = new PillBadge("Home: " + user.getCollege(),
                AppTheme.LAVENDER_SOFT, AppTheme.INDIGO, AppTheme.LAVENDER);
        nameRow.add(homeBadge);

        left.add(nameRow);
        left.add(Box.createVerticalStrut(6));

        JLabel tagline = new JLabel("Discover Beyond Your Campus — Find events, workshops, and clubs across colleges.");
        tagline.setFont(AppTheme.BODY);
        tagline.setForeground(AppTheme.MUTED);
        left.add(tagline);

        hero.add(left, BorderLayout.CENTER);

        InitialsAvatar avatar = new InitialsAvatar(user.getFullName(), 52, AppTheme.INDIGO, Color.WHITE);
        hero.add(avatar, BorderLayout.EAST);

        return hero;
    }

    private JPanel buildQuickActionsBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        bar.add(makePillBtn("📅  Explore Events", () -> mainFrame.showCard(MainFrame.EVENTS)));
        bar.add(makePillBtn("🏛  Colleges", () -> mainFrame.showCard(MainFrame.COLLEGES)));
        bar.add(makePillBtn("👥  Clubs & Activities", () -> mainFrame.showCard(MainFrame.CLUBS)));
        bar.add(makePillBtn("📢  Notices", () -> mainFrame.showCard(MainFrame.NOTICES)));
        bar.add(makePillBtn("★  My Bookmarks", () -> mainFrame.showCard(MainFrame.BOOKMARKS)));
        bar.add(makePillBtn("📝  My Registrations", () -> mainFrame.showCard(MainFrame.REGISTRATIONS)));
        bar.add(makePillBtn("👤  Profile", () -> mainFrame.showCard(MainFrame.PROFILE)));

        return bar;
    }

    private ModernButton makePillBtn(String title, Runnable action) {
        ModernButton btn = new ModernButton(title, ModernButton.Variant.SECONDARY);
        btn.setCornerRadius(20);
        btn.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        AppTheme.styleField(searchField);
        searchField.setFont(AppTheme.BODY);
        searchField.setMinimumSize(new Dimension(80, 40));
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setText(SEARCH_HINT);
        searchField.setForeground(AppTheme.MUTED);
        searchField.setToolTipText("Enter an event title, category, or college name");
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingSearchHint) {
                    searchField.setText("");
                    searchField.setForeground(AppTheme.TEXT);
                    showingSearchHint = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(SEARCH_HINT);
                    searchField.setForeground(AppTheme.MUTED);
                    showingSearchHint = true;
                }
            }
        });

        ModernButton searchBtn = new ModernButton("Search", ModernButton.Variant.PRIMARY);
        searchBtn.setPreferredSize(new Dimension(110, 40));
        searchBtn.addActionListener(e -> performDashboardSearch());
        searchField.addActionListener(e -> performDashboardSearch());

        bar.add(searchField, BorderLayout.CENTER);
        bar.add(searchBtn, BorderLayout.EAST);
        return bar;
    }

    private void performDashboardSearch() {
        String query = showingSearchHint ? "" : searchField.getText().trim();
        if (!query.isEmpty()) {
            mainFrame.searchFromDashboard(query);
        } else {
            searchField.requestFocusInWindow();
        }
    }

    private void addHighlightSection(String title, String subtitle, JPanel body, Runnable onExplore) {
        // Use the active theme so this section does not become a white block in dark mode.
        RoundedCard wrap = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel titleBadgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleBadgeRow.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setFont(AppTheme.HEADING.deriveFont(Font.BOLD, 16f));
        label.setForeground(AppTheme.TEXT);
        titleBadgeRow.add(label);

        PillBadge featureBadge = new PillBadge("INTER-COLLEGE", new Color(248, 240, 222), AppTheme.ACCENT.darker());
        titleBadgeRow.add(featureBadge);
        left.add(titleBadgeRow);

        left.add(Box.createVerticalStrut(4));
        JLabel subLabel = AppTheme.muted(subtitle);
        left.add(subLabel);

        headerRow.add(left, BorderLayout.WEST);

        if (onExplore != null) {
            ModernButton exploreAll = new ModernButton("View All Inter-College →", ModernButton.Variant.GHOST);
            exploreAll.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
            exploreAll.setForeground(AppTheme.INDIGO);
            exploreAll.addActionListener(e -> onExplore.run());
            headerRow.add(exploreAll, BorderLayout.EAST);
        }

        wrap.add(headerRow);
        wrap.add(Box.createVerticalStrut(14));

        body.setOpaque(false);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(body);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        content.add(wrap);
    }

    private void addSection(String title, String subtitle, JPanel body) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrap.add(UiUtil.sectionTitle(title, subtitle));
        wrap.add(Box.createVerticalStrut(12));

        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(body);

        content.add(wrap);
    }

    private JPanel buildUpcomingSoonPanel() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM yyyy");
        List<Event> soon = DummyData.getUpcomingSoon();
        if (soon == null || soon.isEmpty()) {
            list.add(new EmptyState("📅", "No Events This Week",
                    "There are no scheduled events in the next 7 days.", null, null));
            return list;
        }

        LocalDate today = LocalDate.now();
        for (int i = 0; i < Math.min(3, soon.size()); i++) {
            final Event event = soon.get(i);
            long days = ChronoUnit.DAYS.between(today, event.getDate());
            String remaining = days == 0 ? "Today" : days == 1 ? "Tomorrow" : days + " days remaining";

            RoundedCard card = new RoundedCard(10, AppTheme.CARD, AppTheme.BORDER);
            card.setHoverable(true);
            card.setLayout(new BorderLayout(14, 0));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel mid = new JPanel();
            mid.setOpaque(false);
            mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

            JLabel name = new JLabel(event.getEventName());
            name.setFont(AppTheme.BOLD.deriveFont(14f));
            name.setForeground(AppTheme.TEXT);
            mid.add(name);

            JLabel sub = new JLabel(event.getCollege() + "  ·  " + event.getDate().format(fmt) + "  ·  " + event.getCategory());
            sub.setFont(AppTheme.SMALL);
            sub.setForeground(AppTheme.MUTED);
            mid.add(sub);

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            right.setOpaque(false);

            PillBadge daysBadge = new PillBadge(remaining, new Color(236, 244, 255), AppTheme.INDIGO);
            right.add(daysBadge);

            ModernButton viewBtn = new ModernButton("Details", ModernButton.Variant.SECONDARY);
            viewBtn.setCornerRadius(6);
            viewBtn.addActionListener(e -> mainFrame.showEventDetails(event, MainFrame.STUDENT_DASHBOARD));
            right.add(viewBtn);

            card.add(mid, BorderLayout.CENTER);
            card.add(right, BorderLayout.EAST);

            list.add(card);
            list.add(Box.createVerticalStrut(8));
        }
        return list;
    }

    private JPanel buildNoticesPreview() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        int count = Math.min(3, DummyData.notices.size());
        if (count == 0) {
            list.add(new EmptyState("📢", "No Notices Available", "Check back soon for new announcements.", null, null));
            return list;
        }

        for (int i = 0; i < count; i++) {
            Notice notice = DummyData.notices.get(i);
            list.add(new NoticeCardPanel(notice));
            list.add(Box.createVerticalStrut(10));
        }
        return list;
    }

    private List<Event> take(List<Event> source, int n) {
        List<Event> copy = new ArrayList<Event>();
        if (source == null) return copy;
        int limit = Math.min(n, source.size());
        for (int i = 0; i < limit; i++) {
            copy.add(source.get(i));
        }
        return copy;
    }
}
