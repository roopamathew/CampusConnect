package ui;

import data.DummyData;
import model.College;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dedicated campus event discovery screen displaying all events hosted by a selected college.
 */
public class CollegeEventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel inner;
    private String collegeName = "";

    public CollegeEventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(AppTheme.BG);
        inner.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        add(UiUtil.scroll(inner), BorderLayout.CENTER);
    }

    public void showCollege(String collegeName) {
        this.collegeName = collegeName != null ? collegeName : "";
        rebuild();
    }

    private void rebuild() {
        inner.removeAll();

        // 1. Back button bar
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setOpaque(false);
        navBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        ModernButton backBtn = new ModernButton("← Back to Colleges", ModernButton.Variant.SECONDARY);
        backBtn.setCornerRadius(6);
        backBtn.addActionListener(e -> mainFrame.showCard(MainFrame.COLLEGES));
        navBar.add(backBtn);
        inner.add(navBar);
        inner.add(Box.createVerticalStrut(14));

        // Find college object for metadata
        College matchedCollege = null;
        for (College col : DummyData.colleges) {
            if (col.getCollegeName().equalsIgnoreCase(collegeName)) {
                matchedCollege = col;
                break;
            }
        }

        // 2. Campus Hero Card
        RoundedCard heroCard = new RoundedCard(14, AppTheme.NAVY, AppTheme.NAVY_DARK);
        heroCard.setLayout(new BorderLayout(16, 0));
        heroCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        heroCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        heroCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        InitialsAvatar crest = new InitialsAvatar(collegeName, 56, AppTheme.INDIGO, Color.WHITE);
        heroCard.add(crest, BorderLayout.WEST);

        JPanel heroMid = new JPanel();
        heroMid.setOpaque(false);
        heroMid.setLayout(new BoxLayout(heroMid, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(collegeName);
        title.setFont(AppTheme.TITLE.deriveFont(20f));
        title.setForeground(Color.WHITE);
        heroMid.add(title);
        heroMid.add(Box.createVerticalStrut(4));

        String locText = matchedCollege != null
                ? matchedCollege.getLocation() + "  ·  " + matchedCollege.getEmail()
                : "Partner Campus";
        JLabel locLbl = new JLabel(locText);
        locLbl.setFont(AppTheme.BODY);
        locLbl.setForeground(AppTheme.LAVENDER);
        heroMid.add(locLbl);

        if (matchedCollege != null && matchedCollege.getDescription() != null) {
            heroMid.add(Box.createVerticalStrut(4));
            JLabel descLbl = new JLabel("<html><div style='width:580px;color:#C4BAD8;font-size:11.5px;'>"
                    + matchedCollege.getDescription() + "</div></html>");
            heroMid.add(descLbl);
        }

        heroCard.add(heroMid, BorderLayout.CENTER);
        inner.add(heroCard);
        inner.add(Box.createVerticalStrut(20));

        // 3. Section Title with Events Count
        List<Event> events = DummyData.getEventsByCollege(collegeName);
        JPanel sectionRow = new JPanel(new BorderLayout());
        sectionRow.setOpaque(false);
        sectionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel("Hosted Events & Competitions (" + events.size() + ")");
        heading.setFont(AppTheme.HEADING);
        heading.setForeground(AppTheme.NAVY);
        sectionRow.add(heading, BorderLayout.WEST);

        inner.add(sectionRow);
        inner.add(Box.createVerticalStrut(12));

        // 4. Events Listing
        JPanel eventsList = EventCardPanel.listOf(events, mainFrame, MainFrame.COLLEGE_EVENTS);
        eventsList.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(eventsList);

        inner.revalidate();
        inner.repaint();
    }
}
