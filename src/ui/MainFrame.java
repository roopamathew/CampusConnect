package ui;

import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Single window after login. Sidebar buttons switch CardLayout panels.
 */
public class MainFrame extends JFrame {

    public static final String STUDENT_DASHBOARD = "STUDENT_DASHBOARD";
    public static final String EVENTS = "EVENTS";
    public static final String EVENT_DETAILS = "EVENT_DETAILS";
    public static final String REGISTRATIONS = "REGISTRATIONS";
    public static final String BOOKMARKS = "BOOKMARKS";
    public static final String NOTICES = "NOTICES";
    public static final String COLLEGES = "COLLEGES";
    public static final String COLLEGE_EVENTS = "COLLEGE_EVENTS";
    public static final String CLUBS = "CLUBS";
    public static final String PROFILE = "PROFILE";
    public static final String ADMIN_DASHBOARD = "ADMIN_DASHBOARD";
    public static final String MANAGE_EVENTS = "MANAGE_EVENTS";
    public static final String MANAGE_NOTICES = "MANAGE_NOTICES";
    public static final String MANAGE_COLLEGES = "MANAGE_COLLEGES";
    public static final String MANAGE_CLUBS = "MANAGE_CLUBS";
    public static final String ADMIN_REGISTRATIONS = "ADMIN_REGISTRATIONS";

    private final User currentUser;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private StudentDashboard studentDashboard;
    private EventsPanel eventsPanel;
    private EventDetailsPanel eventDetailsPanel;
    private RegistrationPanel registrationPanel;
    private BookmarkPanel bookmarkPanel;
    private NoticePanel noticePanel;
    private CollegePanel collegePanel;
    private CollegeEventsPanel collegeEventsPanel;
    private ClubPanel clubPanel;
    private AdminDashboard adminDashboard;
    private ManageEventsPanel manageEventsPanel;
    private ManageNoticesPanel manageNoticesPanel;
    private ManageCollegesPanel manageCollegesPanel;
    private ManageClubsPanel manageClubsPanel;
    private AdminRegistrationsPanel adminRegistrationsPanel;

    public MainFrame(User currentUser) {
        this.currentUser = currentUser;
        setTitle("CampusConnect – Discover Beyond Your Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(980, 640));

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        buildCards();
        root.add(cards, BorderLayout.CENTER);
        setContentPane(root);

        if ("ADMIN".equals(currentUser.getRole())) {
            showCard(ADMIN_DASHBOARD);
        } else {
            showCard(STUDENT_DASHBOARD);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("CAMPUSCONNECT");
        name.setFont(AppTheme.BRAND);
        name.setForeground(AppTheme.NAVY);
        JLabel tagline = new JLabel("ADMIN".equals(currentUser.getRole())
                ? "Admin  ·  Discover Beyond Your Campus"
                : "Discover Beyond Your Campus");
        tagline.setFont(AppTheme.SMALL);
        tagline.setForeground(AppTheme.TEAL);
        brand.add(name);
        brand.add(tagline);

        JPanel welcomeBlock = new JPanel();
        welcomeBlock.setOpaque(false);
        welcomeBlock.setLayout(new BoxLayout(welcomeBlock, BoxLayout.Y_AXIS));
        JLabel welcome = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcome.setFont(AppTheme.BOLD);
        welcome.setForeground(AppTheme.TEXT);
        welcome.setAlignmentX(RIGHT_ALIGNMENT);
        JLabel college = new JLabel(currentUser.getCollege());
        college.setFont(AppTheme.SMALL);
        college.setForeground(AppTheme.TEAL);
        college.setAlignmentX(RIGHT_ALIGNMENT);
        welcomeBlock.add(welcome);
        welcomeBlock.add(college);

        header.add(brand, BorderLayout.WEST);
        header.add(welcomeBlock, BorderLayout.EAST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.NAVY);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        if ("ADMIN".equals(currentUser.getRole())) {
            addNav(sidebar, "Dashboard", ADMIN_DASHBOARD);
            addNav(sidebar, "Manage Events", MANAGE_EVENTS);
            addNav(sidebar, "Manage Notices", MANAGE_NOTICES);
            addNav(sidebar, "Manage Colleges", MANAGE_COLLEGES);
            addNav(sidebar, "Manage Clubs", MANAGE_CLUBS);
            addNav(sidebar, "Registrations", ADMIN_REGISTRATIONS);
        } else {
            addNav(sidebar, "Dashboard", STUDENT_DASHBOARD);
            addNav(sidebar, "Explore Events", EVENTS);
            addNav(sidebar, "My Registrations", REGISTRATIONS);
            addNav(sidebar, "Bookmarks", BOOKMARKS);
            addNav(sidebar, "Notices", NOTICES);
            addNav(sidebar, "Explore Colleges", COLLEGES);
            addNav(sidebar, "Clubs & Activities", CLUBS);
            addNav(sidebar, "Profile", PROFILE);
        }

        sidebar.add(Box.createVerticalGlue());
        JButton logout = AppTheme.navButton("Logout");
        logout.addActionListener(e -> logout());
        sidebar.add(logout);
        return sidebar;
    }

    private void addNav(JPanel sidebar, String label, final String card) {
        JButton button = AppTheme.navButton(label);
        button.addActionListener(e -> showCard(card));
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(4));
    }

    private void buildCards() {
        if ("ADMIN".equals(currentUser.getRole())) {
            adminDashboard = new AdminDashboard(this);
            manageEventsPanel = new ManageEventsPanel(this);
            manageNoticesPanel = new ManageNoticesPanel(this);
            manageCollegesPanel = new ManageCollegesPanel(this);
            manageClubsPanel = new ManageClubsPanel(this);
            adminRegistrationsPanel = new AdminRegistrationsPanel(this);
            eventDetailsPanel = new EventDetailsPanel(this);
            cards.add(adminDashboard, ADMIN_DASHBOARD);
            cards.add(manageEventsPanel, MANAGE_EVENTS);
            cards.add(manageNoticesPanel, MANAGE_NOTICES);
            cards.add(manageCollegesPanel, MANAGE_COLLEGES);
            cards.add(manageClubsPanel, MANAGE_CLUBS);
            cards.add(adminRegistrationsPanel, ADMIN_REGISTRATIONS);
            cards.add(eventDetailsPanel, EVENT_DETAILS);
        } else {
            studentDashboard = new StudentDashboard(this);
            eventsPanel = new EventsPanel(this);
            eventDetailsPanel = new EventDetailsPanel(this);
            registrationPanel = new RegistrationPanel(this);
            bookmarkPanel = new BookmarkPanel(this);
            noticePanel = new NoticePanel(this);
            collegePanel = new CollegePanel(this);
            collegeEventsPanel = new CollegeEventsPanel(this);
            clubPanel = new ClubPanel(this);
            cards.add(studentDashboard, STUDENT_DASHBOARD);
            cards.add(eventsPanel, EVENTS);
            cards.add(eventDetailsPanel, EVENT_DETAILS);
            cards.add(registrationPanel, REGISTRATIONS);
            cards.add(bookmarkPanel, BOOKMARKS);
            cards.add(noticePanel, NOTICES);
            cards.add(collegePanel, COLLEGES);
            cards.add(collegeEventsPanel, COLLEGE_EVENTS);
            cards.add(clubPanel, CLUBS);
            cards.add(new ProfilePanel(this), PROFILE);
        }
    }

    public void showCard(String name) {
        if (STUDENT_DASHBOARD.equals(name) && studentDashboard != null) {
            studentDashboard.refresh();
        } else if (EVENTS.equals(name) && eventsPanel != null) {
            eventsPanel.refresh();
        } else if (REGISTRATIONS.equals(name) && registrationPanel != null) {
            registrationPanel.refresh();
        } else if (BOOKMARKS.equals(name) && bookmarkPanel != null) {
            bookmarkPanel.refresh();
        } else if (NOTICES.equals(name) && noticePanel != null) {
            noticePanel.refresh();
        } else if (COLLEGES.equals(name) && collegePanel != null) {
            collegePanel.refresh();
        } else if (CLUBS.equals(name) && clubPanel != null) {
            clubPanel.refresh();
        } else if (ADMIN_DASHBOARD.equals(name) && adminDashboard != null) {
            adminDashboard.refresh();
        } else if (MANAGE_EVENTS.equals(name) && manageEventsPanel != null) {
            manageEventsPanel.refresh();
        } else if (MANAGE_NOTICES.equals(name) && manageNoticesPanel != null) {
            manageNoticesPanel.refresh();
        } else if (MANAGE_COLLEGES.equals(name) && manageCollegesPanel != null) {
            manageCollegesPanel.refresh();
        } else if (MANAGE_CLUBS.equals(name) && manageClubsPanel != null) {
            manageClubsPanel.refresh();
        } else if (ADMIN_REGISTRATIONS.equals(name) && adminRegistrationsPanel != null) {
            adminRegistrationsPanel.refresh();
        }
        cardLayout.show(cards, name);
    }

    public void showEventDetails(Event event, String backCard) {
        eventDetailsPanel.showEvent(event, backCard);
        cardLayout.show(cards, EVENT_DETAILS);
    }

    public void showCollegeEvents(String collegeName) {
        collegeEventsPanel.showCollege(collegeName);
        cardLayout.show(cards, COLLEGE_EVENTS);
    }

    public void searchFromDashboard(String query) {
        eventsPanel.applySearch(query);
        showCard(EVENTS);
    }

    public void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
