package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Redesigned Bookmarks panel with rich event cards, instant removal,
 * registration actions, and clean empty states.
 */
public class BookmarkPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public BookmarkPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        page.add(UiUtil.pageHeader("My Saved Bookmarks",
                "Events you have flagged to follow and attend.", null));
        page.add(Box.createVerticalStrut(18));

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

        if (events == null || events.isEmpty()) {
            listHost.add(new EmptyState("★", "No Saved Bookmarks",
                    "You have not bookmarked any events yet. Browse events and click Bookmark to save them here.",
                    "Discover Events", () -> mainFrame.showCard(MainFrame.EVENTS)));
        } else {
            for (int i = 0; i < events.size(); i++) {
                final Event event = events.get(i);
                boolean hasImg = event.hasImage() && ImageUtil.isValidImage(event.getImagePath());
                RoundedCard card = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
                card.setHoverable(true);
                card.setLayout(new BorderLayout(12, 10));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
                card.setAlignmentX(LEFT_ALIGNMENT);

                if (hasImg) {
                    javax.swing.JComponent img = ImageUtil.createImageViewer(event.getImagePath(), 90, 90, 8);
                    if (img != null) {
                        card.add(img, BorderLayout.WEST);
                    }
                }

                // Top
                JPanel top = new JPanel(new BorderLayout());
                top.setOpaque(false);

                JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                tags.setOpaque(false);
                tags.add(PillBadge.category(event.getCategory()));
                tags.add(PillBadge.status(event.getStatus()));
                top.add(tags, BorderLayout.WEST);

                JLabel dateLbl = new JLabel("📅  " + event.getDate().format(DATE_FMT));
                dateLbl.setFont(AppTheme.SMALL);
                dateLbl.setForeground(AppTheme.MUTED);
                top.add(dateLbl, BorderLayout.EAST);
                card.add(top, BorderLayout.NORTH);

                // Center
                JPanel mid = new JPanel();
                mid.setOpaque(false);
                mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

                JLabel title = new JLabel(event.getEventName());
                title.setFont(AppTheme.HEADING);
                title.setForeground(AppTheme.TEXT);
                mid.add(title);
                mid.add(Box.createVerticalStrut(4));

                JLabel location = new JLabel(event.getCollege() + "  ·  " + event.getVenue() + ", " + event.getLocation());
                location.setFont(AppTheme.BODY);
                location.setForeground(AppTheme.MUTED);
                mid.add(location);

                card.add(mid, BorderLayout.CENTER);

                // South actions
                JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                actions.setOpaque(false);

                ModernButton viewBtn = new ModernButton("View Details", ModernButton.Variant.PRIMARY);
                viewBtn.setCornerRadius(6);
                viewBtn.addActionListener(e -> mainFrame.showEventDetails(event, MainFrame.BOOKMARKS));

                ModernButton removeBtn = new ModernButton("Remove Bookmark", ModernButton.Variant.DANGER);
                removeBtn.setCornerRadius(6);
                removeBtn.addActionListener(e -> {
                    DummyData.removeBookmark(email, event.getEventId());
                    JOptionPane.showMessageDialog(BookmarkPanel.this,
                            "Bookmark removed for " + event.getEventName() + ".",
                            "Bookmarks", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                });

                actions.add(viewBtn);
                actions.add(removeBtn);
                card.add(actions, BorderLayout.SOUTH);

                listHost.add(card);
                listHost.add(Box.createVerticalStrut(12));
            }
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
