package ui;

import data.DummyData;
import model.Notice;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class NoticePanel extends JPanel {

    private final JPanel listHost;

    public NoticePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        JPanel page = UiUtil.page("Notice Board", "Stay updated with campus announcements.");
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
        if (DummyData.notices.isEmpty()) {
            listHost.add(AppTheme.muted("No notices available."));
        } else {
            for (int i = 0; i < DummyData.notices.size(); i++) {
                Notice notice = DummyData.notices.get(i);
                listHost.add(new NoticeCardPanel(notice));
                listHost.add(Box.createVerticalStrut(10));
            }
        }
        listHost.revalidate();
        listHost.repaint();
    }
}
