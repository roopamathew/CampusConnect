package ui;

import model.Event;
import model.User;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Main application window after authentication.
 * Features unified responsive header, permission-scoped sidebar navigation,
 * and robust CardLayout screen routing for Students, College Admins, and Super Admin.
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
    public static final String SUPER_ADMIN_DASHBOARD = "SUPER_ADMIN_DASHBOARD";
    public static final String ADMIN_REQUESTS = "ADMIN_REQUESTS";
    public static final String MANAGE_ADMINS = "MANAGE_ADMINS";

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
    private ProfilePanel profilePanel;

    private AdminDashboard adminDashboard;
    private ManageEventsPanel manageEventsPanel;
    private ManageNoticesPanel manageNoticesPanel;
    private ManageCollegesPanel manageCollegesPanel;
    private ManageClubsPanel manageClubsPanel;
    private AdminRegistrationsPanel adminRegistrationsPanel;

    private SuperAdminDashboard superAdminDashboard;
    private AdminRequestsPanel adminRequestsPanel;
    private ManageAdminsPanel manageAdminsPanel;

    private final Map<String, JButton> navButtons = new LinkedHashMap<String, JButton>();
    private String lastNavCard;
    private JPanel headerPanel;

    public MainFrame(User currentUser) {
        this.currentUser = currentUser;
        setTitle("CampusConnect – Discover Beyond Your Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 780);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(980, 640));

        JPanel root = new JPanel(new BorderLayout());
        headerPanel = buildHeader();
        root.add(headerPanel, BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        buildCards();
        root.add(cards, BorderLayout.CENTER);
        setContentPane(root);

        if ("SUPER_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            showCard(SUPER_ADMIN_DASHBOARD);
        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            showCard(ADMIN_DASHBOARD);
        } else {
            showCard(STUDENT_DASHBOARD);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshHeader() {
        if (headerPanel != null) {
            headerPanel.removeAll();
            JPanel updated = buildHeader();
            headerPanel.setLayout(new BorderLayout());
            headerPanel.add(updated.getComponent(0), BorderLayout.WEST);
            headerPanel.add(updated.getComponent(1), BorderLayout.EAST);
            headerPanel.revalidate();
            headerPanel.repaint();
        }
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)));

        // Left Brand
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JPanel brandTitleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brandTitleRow.setOpaque(false);

        JLabel name = new JLabel("CAMPUSCONNECT");
        name.setFont(AppTheme.BRAND.deriveFont(20f));
        name.setForeground(AppTheme.NAVY);
        brandTitleRow.add(name);

        brandTitleRow.add(PillBadge.role(currentUser.getRole()));
        brand.add(brandTitleRow);

        String subtitleText;
        if ("SUPER_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            subtitleText = "Platform Governance  ·  Discover Beyond Your Campus";
        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            subtitleText = "Campus Administration (" + currentUser.getCollege() + ")  ·  Discover Beyond Your Campus";
        } else {
            subtitleText = "Discover Beyond Your Campus";
        }

        JLabel tagline = new JLabel(subtitleText);
        tagline.setFont(AppTheme.SMALL);
        tagline.setForeground(AppTheme.MUTED);
        brand.add(tagline);

        // Right User identity
        JPanel welcomeBlock = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        welcomeBlock.setOpaque(false);

        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel(currentUser.getFullName());
        welcome.setFont(AppTheme.BOLD.deriveFont(14f));
        welcome.setForeground(AppTheme.NAVY);
        welcome.setAlignmentX(RIGHT_ALIGNMENT);

        String metaCollege = (currentUser.getCollege() != null && !currentUser.getCollege().isEmpty())
                ? currentUser.getCollege() : "Institutional Member";
        JLabel college = new JLabel(metaCollege);
        college.setFont(AppTheme.SMALL);
        college.setForeground(AppTheme.MUTED);
        college.setAlignmentX(RIGHT_ALIGNMENT);

        textStack.add(welcome);
        textStack.add(college);

        InitialsAvatar headerAvatar = new InitialsAvatar(currentUser.getFullName(), 38, AppTheme.INDIGO, Color.WHITE);

        welcomeBlock.add(textStack);
        welcomeBlock.add(headerAvatar);

        header.add(brand, BorderLayout.WEST);
        header.add(welcomeBlock, BorderLayout.EAST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.NAVY);
        sidebar.setPreferredSize(new Dimension(236, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        if ("SUPER_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            addNav(sidebar, "Dashboard", SUPER_ADMIN_DASHBOARD);
            addNav(sidebar, "Admin Requests", ADMIN_REQUESTS);
            addNav(sidebar, "Manage Admins", MANAGE_ADMINS);
            addNav(sidebar, "Manage Colleges", MANAGE_COLLEGES);
            addNav(sidebar, "Manage Events", MANAGE_EVENTS);
            addNav(sidebar, "My Profile", PROFILE);
        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            addNav(sidebar, "Dashboard", ADMIN_DASHBOARD);
            addNav(sidebar, "Manage Events", MANAGE_EVENTS);
            addNav(sidebar, "Manage Notices", MANAGE_NOTICES);
            addNav(sidebar, "Manage Clubs", MANAGE_CLUBS);
            addNav(sidebar, "Registrations", ADMIN_REGISTRATIONS);
            addNav(sidebar, "My Profile", PROFILE);
        } else {
            addNav(sidebar, "Dashboard", STUDENT_DASHBOARD);
            addNav(sidebar, "Explore Events", EVENTS);
            addNav(sidebar, "My Registrations", REGISTRATIONS);
            addNav(sidebar, "Saved Bookmarks", BOOKMARKS);
            addNav(sidebar, "Notice Board", NOTICES);
            addNav(sidebar, "Explore Colleges", COLLEGES);
            addNav(sidebar, "Clubs & Activities", CLUBS);
            addNav(sidebar, "My Profile", PROFILE);
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logout = AppTheme.navButton("← Sign Out");
        logout.addActionListener(e -> logout());
        sidebar.add(logout);
        return sidebar;
    }

    private void addNav(JPanel sidebar, String label, final String card) {
        JButton button = AppTheme.navButton(label);
        button.addActionListener(e -> showCard(card));
        navButtons.put(card, button);
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(4));
    }

    private void highlightNav(String card) {
        String key = card;
        if (EVENT_DETAILS.equals(card)) {
            key = lastNavCard;
        } else if (COLLEGE_EVENTS.equals(card)) {
            key = COLLEGES;
        }
        if (key == null || !navButtons.containsKey(key)) {
            return;
        }
        lastNavCard = key;
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            AppTheme.setNavActive(entry.getValue(), entry.getKey().equals(key));
        }
    }

    private void buildCards() {
        eventDetailsPanel = new EventDetailsPanel(this);
        profilePanel = new ProfilePanel(this);

        if ("SUPER_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            superAdminDashboard = new SuperAdminDashboard(this);
            adminRequestsPanel = new AdminRequestsPanel(this);
            manageAdminsPanel = new ManageAdminsPanel(this);
            manageCollegesPanel = new ManageCollegesPanel(this);
            manageEventsPanel = new ManageEventsPanel(this);

            cards.add(superAdminDashboard, SUPER_ADMIN_DASHBOARD);
            cards.add(adminRequestsPanel, ADMIN_REQUESTS);
            cards.add(manageAdminsPanel, MANAGE_ADMINS);
            cards.add(manageCollegesPanel, MANAGE_COLLEGES);
            cards.add(manageEventsPanel, MANAGE_EVENTS);
            cards.add(eventDetailsPanel, EVENT_DETAILS);
            cards.add(profilePanel, PROFILE);
        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            adminDashboard = new AdminDashboard(this);
            manageEventsPanel = new ManageEventsPanel(this);
            manageNoticesPanel = new ManageNoticesPanel(this);
            manageCollegesPanel = new ManageCollegesPanel(this);
            manageClubsPanel = new ManageClubsPanel(this);
            adminRegistrationsPanel = new AdminRegistrationsPanel(this);

            cards.add(adminDashboard, ADMIN_DASHBOARD);
            cards.add(manageEventsPanel, MANAGE_EVENTS);
            cards.add(manageNoticesPanel, MANAGE_NOTICES);
            cards.add(manageCollegesPanel, MANAGE_COLLEGES);
            cards.add(manageClubsPanel, MANAGE_CLUBS);
            cards.add(adminRegistrationsPanel, ADMIN_REGISTRATIONS);
            cards.add(eventDetailsPanel, EVENT_DETAILS);
            cards.add(profilePanel, PROFILE);
        } else {
            studentDashboard = new StudentDashboard(this);
            eventsPanel = new EventsPanel(this);
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
            cards.add(profilePanel, PROFILE);
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
        } else if (PROFILE.equals(name) && profilePanel != null) {
            profilePanel.refresh();
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
        } else if (SUPER_ADMIN_DASHBOARD.equals(name) && superAdminDashboard != null) {
            superAdminDashboard.refresh();
        } else if (ADMIN_REQUESTS.equals(name) && adminRequestsPanel != null) {
            adminRequestsPanel.refresh();
        } else if (MANAGE_ADMINS.equals(name) && manageAdminsPanel != null) {
            manageAdminsPanel.refresh();
        }

        highlightNav(name);
        cardLayout.show(cards, name);
    }

    public void showEventDetails(Event event, String backCard) {
        eventDetailsPanel.showEvent(event, backCard);
        if (backCard != null && navButtons.containsKey(backCard)) {
            highlightNav(backCard);
        } else {
            highlightNav(EVENT_DETAILS);
        }
        cardLayout.show(cards, EVENT_DETAILS);
    }

    public void showCollegeEvents(String collegeName) {
        if (collegeEventsPanel != null) {
            collegeEventsPanel.showCollege(collegeName);
            highlightNav(COLLEGE_EVENTS);
            cardLayout.show(cards, COLLEGE_EVENTS);
        }
    }

    public void searchFromDashboard(String query) {
        if (eventsPanel != null) {
            eventsPanel.applySearch(query);
            showCard(EVENTS);
        }
    }

    public void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
