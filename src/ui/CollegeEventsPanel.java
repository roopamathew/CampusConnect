package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CollegeEventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel inner;
    private String collegeName = "";

    public CollegeEventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        inner = UiUtil.page("College Events", null);
        add(UiUtil.scroll(inner), BorderLayout.CENTER);
    }

    public void showCollege(String collegeName) {
        this.collegeName = collegeName;
        rebuild();
    }

    private void rebuild() {
        inner.removeAll();
        javax.swing.JLabel heading = new javax.swing.JLabel("Events at " + collegeName);
        heading.setFont(AppTheme.TITLE);
        heading.setForeground(AppTheme.NAVY);
        heading.setAlignmentX(LEFT_ALIGNMENT);
        inner.add(heading);
        inner.add(AppTheme.muted("Discover Beyond Your Campus"));
        inner.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);
        JButton back = AppTheme.secondaryButton("BACK");
        back.addActionListener(e -> mainFrame.showCard(MainFrame.COLLEGES));
        actions.add(back);
        inner.add(actions);
        inner.add(Box.createVerticalStrut(12));

        List<Event> events = DummyData.getEventsByCollege(collegeName);
        inner.add(EventCardPanel.listOf(events, mainFrame, MainFrame.COLLEGE_EVENTS));
        inner.revalidate();
        inner.repaint();
    }
}
