package ui;

import data.DummyData;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminDashboard extends JPanel {

    private final JPanel content;

    public AdminDashboard(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        content = UiUtil.page("Admin Dashboard", "Overview of CampusConnect activity.");
        add(UiUtil.scroll(content), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        content.removeAll();
        JLabel heading = new JLabel("Admin Dashboard");
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);
        heading.setAlignmentX(LEFT_ALIGNMENT);
        content.add(heading);
        content.add(AppTheme.muted("CAMPUSCONNECT  ·  Discover Beyond Your Campus"));
        content.add(Box.createVerticalStrut(16));
        content.add(UiUtil.statsRow(
                UiUtil.statCard("Total Events", String.valueOf(DummyData.events.size()), AppTheme.TEAL),
                UiUtil.statCard("Upcoming Events", String.valueOf(DummyData.countUpcomingEvents()), AppTheme.NAVY),
                UiUtil.statCard("Total Colleges", String.valueOf(DummyData.colleges.size()), AppTheme.ACCENT),
                UiUtil.statCard("Total Registrations", String.valueOf(DummyData.registrations.size()), AppTheme.SUCCESS)
        ));
        content.add(Box.createVerticalStrut(12));
        content.add(UiUtil.statsRow(
                UiUtil.statCard("Total Notices", String.valueOf(DummyData.notices.size()), AppTheme.TEAL),
                UiUtil.statCard("Total Clubs", String.valueOf(DummyData.clubs.size()), AppTheme.NAVY),
                UiUtil.statCard("Students", String.valueOf(countStudents()), AppTheme.ACCENT),
                UiUtil.statCard("Bookmarks", String.valueOf(countBookmarks()), AppTheme.SUCCESS)
        ));
        content.revalidate();
        content.repaint();
    }

    private int countStudents() {
        int count = 0;
        for (int i = 0; i < DummyData.users.size(); i++) {
            if ("STUDENT".equals(DummyData.users.get(i).getRole())) {
                count++;
            }
        }
        return count;
    }

    private int countBookmarks() {
        int count = 0;
        for (java.util.List<Integer> ids : DummyData.bookmarksByEmail.values()) {
            count += ids.size();
        }
        return count;
    }
}
