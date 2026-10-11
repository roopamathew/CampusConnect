package ui;

import data.DummyData;
import model.Event;
import model.Notice;
import model.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StudentDashboard extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel content;
    private final JTextField searchField = new JTextField();

    public StudentDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        content = UiUtil.page("Dashboard", "Discover events on your campus and beyond.");
        add(UiUtil.scroll(content), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        content.removeAll();
        User user = mainFrame.getCurrentUser();

        JLabel heading = new JLabel("Dashboard");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);
        heading.setAlignmentX(LEFT_ALIGNMENT);
        content.add(heading);
        JLabel welcome = new JLabel("Welcome, " + user.getFullName() + "!");
        welcome.setFont(AppTheme.BOLD);
        welcome.setForeground(AppTheme.TEXT);
        welcome.setAlignmentX(LEFT_ALIGNMENT);
        content.add(welcome);
        content.add(AppTheme.muted(user.getCollege()));
        content.add(Box.createVerticalStrut(16));

        content.add(searchBar());
        content.add(Box.createVerticalStrut(16));

        int upcoming = DummyData.countUpcomingEvents();
        int regs = DummyData.getRegistrationsForEmail(user.getEmail()).size();
        int bookmarks = DummyData.getBookmarkIds(user.getEmail()).size();
        int noticeCount = DummyData.notices.size();
        content.add(UiUtil.statsRow(
                UiUtil.statCard("Upcoming Events", String.valueOf(upcoming), AppTheme.TEAL),
                UiUtil.statCard("My Registrations", String.valueOf(regs), AppTheme.NAVY),
                UiUtil.statCard("My Bookmarks", String.valueOf(bookmarks), AppTheme.ACCENT),
                UiUtil.statCard("New Notices", String.valueOf(noticeCount), AppTheme.SUCCESS)
        ));
        content.add(Box.createVerticalStrut(22));

        addSection("Upcoming Events", EventCardPanel.listOf(take(DummyData.getUpcomingEvents(), 3),
                mainFrame, MainFrame.STUDENT_DASHBOARD));

        JPanel beyond = EventCardPanel.listOf(take(DummyData.getEventsBeyondCampus(user.getCollege()), 4),
                mainFrame, MainFrame.STUDENT_DASHBOARD);
        addHighlightSection("DISCOVER BEYOND YOUR CAMPUS",
                "Explore opportunities happening at other colleges.", beyond);

        addSection("Recommended For You",
                EventCardPanel.listOf(take(DummyData.getRecommendedEvents(user.getInterest()), 3),
                        mainFrame, MainFrame.STUDENT_DASHBOARD));

        addSection("Upcoming Soon", upcomingSoonPanel());

        addSection("Important Notices", noticePreview());

        content.revalidate();
        content.repaint();
    }

    private JPanel searchBar() {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setFont(AppTheme.BODY);
        JButton search = AppTheme.primaryButton("Search");
        search.addActionListener(e -> mainFrame.searchFromDashboard(searchField.getText()));
        bar.add(searchField, BorderLayout.CENTER);
        bar.add(search, BorderLayout.EAST);
        return bar;
    }

    private void addSection(String title, JPanel body) {
        JLabel label = AppTheme.heading(title);
        label.setAlignmentX(LEFT_ALIGNMENT);
        content.add(label);
        content.add(Box.createVerticalStrut(8));
        body.setAlignmentX(LEFT_ALIGNMENT);
        content.add(body);
        content.add(Box.createVerticalStrut(20));
    }

    private void addHighlightSection(String title, String subtitle, JPanel body) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(AppTheme.LAVENDER_SOFT);
        wrap.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(AppTheme.LAVENDER),
                javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        wrap.setAlignmentX(LEFT_ALIGNMENT);
        JLabel label = AppTheme.heading(title);
        label.setForeground(AppTheme.NAVY);
        wrap.add(label);
        wrap.add(AppTheme.muted(subtitle));
        wrap.add(Box.createVerticalStrut(10));
        body.setOpaque(false);
        wrap.add(body);
        content.add(wrap);
        content.add(Box.createVerticalStrut(20));
    }

    private JPanel upcomingSoonPanel() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM yyyy");
        List<Event> soon = DummyData.getUpcomingSoon();
        if (soon.isEmpty()) {
            list.add(AppTheme.muted("No events in the next 7 days."));
            return list;
        }
        LocalDate today = LocalDate.now();
        for (int i = 0; i < soon.size(); i++) {
            Event event = soon.get(i);
            long days = ChronoUnit.DAYS.between(today, event.getDate());
            String remaining = days == 0 ? "Today" : days == 1 ? "1 day remaining" : days + " days remaining";
            JPanel card = new JPanel(new BorderLayout());
            AppTheme.styleCard(card);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            card.setAlignmentX(LEFT_ALIGNMENT);
            JLabel name = new JLabel(event.getEventName());
            name.setFont(AppTheme.BOLD);
            name.setForeground(AppTheme.NAVY);
            JPanel mid = new JPanel();
            mid.setOpaque(false);
            mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
            mid.add(name);
            mid.add(AppTheme.muted(event.getCollege()));
            mid.add(AppTheme.muted(event.getDate().format(fmt)));
            JLabel daysLabel = new JLabel(remaining);
            daysLabel.setFont(AppTheme.BOLD);
            daysLabel.setForeground(AppTheme.TEAL);
            card.add(mid, BorderLayout.CENTER);
            card.add(daysLabel, BorderLayout.EAST);
            list.add(card);
            list.add(Box.createVerticalStrut(8));
        }
        return list;
    }

    private JPanel noticePreview() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        int count = Math.min(3, DummyData.notices.size());
        for (int i = 0; i < count; i++) {
            Notice notice = DummyData.notices.get(i);
            list.add(new NoticeCardPanel(notice));
            list.add(Box.createVerticalStrut(8));
        }
        return list;
    }

    private List<Event> take(List<Event> source, int n) {
        java.util.ArrayList<Event> copy = new java.util.ArrayList<Event>();
        int limit = Math.min(n, source.size());
        for (int i = 0; i < limit; i++) {
            copy.add(source.get(i));
        }
        return copy;
    }
}
