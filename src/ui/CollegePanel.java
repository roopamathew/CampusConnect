package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class CollegePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;

    public CollegePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        JPanel page = UiUtil.page("Explore Colleges", "See what other campuses are organising.");
        listHost = new JPanel();
        listHost.setLayout(new BoxLayout(listHost, BoxLayout.Y_AXIS));
        listHost.setOpaque(false);
        listHost.setAlignmentX(LEFT_ALIGNMENT);
        page.add(listHost);
        add(UiUtil.scroll(page), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        listHost.removeAll();
        for (int i = 0; i < DummyData.colleges.size(); i++) {
            College college = DummyData.colleges.get(i);
            listHost.add(new CollegeCardPanel(college, mainFrame));
            listHost.add(Box.createVerticalStrut(10));
        }
        listHost.revalidate();
        listHost.repaint();
    }
}
