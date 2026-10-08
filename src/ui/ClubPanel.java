package ui;

import data.DummyData;
import model.Club;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ClubPanel extends JPanel {

    private final JPanel listHost;

    public ClubPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        JPanel page = UiUtil.page("Clubs & Activities", "Student communities across campuses.");
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
        for (int i = 0; i < DummyData.clubs.size(); i++) {
            Club club = DummyData.clubs.get(i);
            listHost.add(new ClubCardPanel(club));
            listHost.add(Box.createVerticalStrut(10));
        }
        listHost.revalidate();
        listHost.repaint();
    }
}
