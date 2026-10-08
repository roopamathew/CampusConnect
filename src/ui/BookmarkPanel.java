package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BookmarkPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;

    public BookmarkPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        JPanel page = UiUtil.page("My Bookmarks", "Saved events you want to revisit.");
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
        String email = mainFrame.getCurrentUser().getEmail();
        List<Event> events = DummyData.getBookmarkedEvents(email);
        if (events.isEmpty()) {
            listHost.add(AppTheme.muted("You have no bookmarked events."));
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM yyyy");
            for (int i = 0; i < events.size(); i++) {
                final Event event = events.get(i);
                JPanel card = new JPanel(new BorderLayout());
                AppTheme.styleCard(card);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
                card.setAlignmentX(LEFT_ALIGNMENT);

                JPanel body = new JPanel();
                body.setOpaque(false);
                body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
                JLabel name = new JLabel(event.getEventName());
                name.setFont(AppTheme.HEADING);
                name.setForeground(AppTheme.NAVY);
                body.add(name);
                body.add(AppTheme.muted(event.getCollege() + "  ·  " + event.getCategory()));
                body.add(AppTheme.muted(event.getDate().format(fmt)));

                JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
                actions.setOpaque(false);
                JButton view = AppTheme.primaryButton("View Details");
                JButton remove = AppTheme.dangerButton("Remove Bookmark");
                view.addActionListener(e -> mainFrame.showEventDetails(event, MainFrame.BOOKMARKS));
                remove.addActionListener(e -> {
                    DummyData.removeBookmark(email, event.getEventId());
                    JOptionPane.showMessageDialog(this, "Bookmark removed.",
                            "Bookmarks", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                });
                actions.add(view);
                actions.add(remove);

                card.add(body, BorderLayout.CENTER);
                card.add(actions, BorderLayout.SOUTH);
                listHost.add(card);
                listHost.add(Box.createVerticalStrut(10));
            }
        }
        listHost.revalidate();
        listHost.repaint();
    }
}
