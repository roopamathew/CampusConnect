package ui;

import data.DummyData;
import model.AdminRequest;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Dedicated Super Administrator central dashboard.
 * Provides system-wide governance, application approval queues, and institutional metrics.
 */
public class SuperAdminDashboard extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel content;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMM yyyy");

    public SuperAdminDashboard(MainFrame mainFrame) {
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
        User superAdmin = mainFrame.getCurrentUser();
        if (superAdmin == null) return;

        // 1. HERO BANNER
        RoundedCard hero = new RoundedCard(14, AppTheme.NAVY, AppTheme.NAVY_DARK);
        hero.setLayout(new BorderLayout(16, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        InitialsAvatar avatar = new InitialsAvatar(superAdmin.getFullName(), 54, AppTheme.ACCENT, AppTheme.NAVY_DARK);
        hero.add(avatar, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);

        JLabel welcome = new JLabel("Chief Administrator Control Hub");
        welcome.setFont(AppTheme.TITLE.deriveFont(20f));
        welcome.setForeground(Color.WHITE);
        titleRow.add(welcome);

        PillBadge roleBadge = PillBadge.role(superAdmin.getRole());
        titleRow.add(roleBadge);
        details.add(titleRow);
        details.add(Box.createVerticalStrut(4));

        JLabel scope = new JLabel("Central Platform Governance   ·   Administrator Onboarding & Institutional Oversight");
        scope.setFont(AppTheme.BODY);
        scope.setForeground(AppTheme.LAVENDER);
        details.add(scope);

        hero.add(details, BorderLayout.CENTER);
        content.add(hero);
        content.add(Box.createVerticalStrut(20));

        // 2. LIVE METRICS
        List<AdminRequest> pendingReqs = DummyData.getPendingAdminRequests();
        int pendingCount = pendingReqs.size();
        int adminCount = DummyData.getAdmins().size();
        int collegeCount = DummyData.colleges.size();
        int totalEvents = DummyData.events.size();

        content.add(UiUtil.statsRow(
                UiUtil.modernStatCard("Pending Access Requests", String.valueOf(pendingCount), "Requires approval", new Color(210, 110, 40)),
                UiUtil.modernStatCard("Active College Admins", String.valueOf(adminCount), "Provisioned campus reps", AppTheme.INDIGO),
                UiUtil.modernStatCard("Accredited Campuses", String.valueOf(collegeCount), "Participating colleges", AppTheme.SUCCESS),
                UiUtil.modernStatCard("Total Platform Events", String.valueOf(totalEvents), "All partner colleges", AppTheme.ACCENT)
        ));
        content.add(Box.createVerticalStrut(20));

        // 3. SHORTCUTS
        RoundedCard actionsCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        actionsCard.setLayout(new BorderLayout(12, 0));
        actionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        actionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel actTitle = new JLabel("Quick Administrative Actions:");
        actTitle.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        actTitle.setForeground(AppTheme.NAVY);
        actionsCard.add(actTitle, BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        ModernButton reviewBtn = new ModernButton("Review Requests (" + pendingCount + ")", ModernButton.Variant.PRIMARY);
        reviewBtn.setCornerRadius(6);
        reviewBtn.addActionListener(e -> mainFrame.showCard(MainFrame.ADMIN_REQUESTS));

        ModernButton manageAdminsBtn = new ModernButton("Administrator Accounts", ModernButton.Variant.SECONDARY);
        manageAdminsBtn.setCornerRadius(6);
        manageAdminsBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_ADMINS));

        ModernButton manageCollegesBtn = new ModernButton("Manage Campuses", ModernButton.Variant.SECONDARY);
        manageCollegesBtn.setCornerRadius(6);
        manageCollegesBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MANAGE_COLLEGES));

        btnRow.add(reviewBtn);
        btnRow.add(manageAdminsBtn);
        btnRow.add(manageCollegesBtn);

        actionsCard.add(btnRow, BorderLayout.EAST);
        content.add(actionsCard);
        content.add(Box.createVerticalStrut(22));

        // 4. PENDING REQUESTS PREVIEW
        content.add(UiUtil.sectionTitle("Pending Administrator Applications (" + pendingCount + ")",
                "Applications submitted by college representatives awaiting validation"));
        content.add(Box.createVerticalStrut(12));

        if (pendingReqs.isEmpty()) {
            content.add(new EmptyState("✓", "All Applications Processed",
                    "There are no pending administrator requests at this time.",
                    "View Application History", () -> mainFrame.showCard(MainFrame.ADMIN_REQUESTS)));
        } else {
            for (int i = 0; i < pendingReqs.size(); i++) {
                final AdminRequest req = pendingReqs.get(i);
                RoundedCard reqCard = new RoundedCard(10, AppTheme.CARD, AppTheme.BORDER);
                reqCard.setLayout(new BorderLayout(14, 0));
                reqCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                reqCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel mid = new JPanel();
                mid.setOpaque(false);
                mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

                JLabel name = new JLabel(req.getFullName() + "   —   " + req.getDesignation());
                name.setFont(AppTheme.HEADING.deriveFont(14f));
                name.setForeground(AppTheme.NAVY);
                mid.add(name);

                JLabel sub = new JLabel("🏛  " + req.getCollege() + "   ·   ✉  " + req.getEmail() + "   ·   Applied: " + req.getRequestDate().format(DATE_FMT));
                sub.setFont(AppTheme.SMALL);
                sub.setForeground(AppTheme.MUTED);
                mid.add(sub);

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                right.setOpaque(false);

                ModernButton quickApprove = new ModernButton("✓ Quick Approve", ModernButton.Variant.SUCCESS);
                quickApprove.setCornerRadius(6);
                quickApprove.addActionListener(e -> {
                    boolean ok = DummyData.approveAdminRequest(req, "admin123");
                    if (ok) {
                        JOptionPane.showMessageDialog(SuperAdminDashboard.this,
                                "<html><b>Application Approved!</b><br>Admin account created for <b>" + req.getFullName() + "</b> (" + req.getCollege() + ").</html>",
                                "Approval Confirmed", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    }
                });

                ModernButton fullReview = new ModernButton("Review...", ModernButton.Variant.SECONDARY);
                fullReview.setCornerRadius(6);
                fullReview.addActionListener(e -> mainFrame.showCard(MainFrame.ADMIN_REQUESTS));

                right.add(quickApprove);
                right.add(fullReview);

                reqCard.add(mid, BorderLayout.CENTER);
                reqCard.add(right, BorderLayout.EAST);

                content.add(reqCard);
                content.add(Box.createVerticalStrut(10));
            }
        }

        content.revalidate();
        content.repaint();
    }
}
