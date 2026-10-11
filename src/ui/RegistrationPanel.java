package ui;

import data.DummyData;
import model.Event;
import model.Registration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Redesigned My Registrations panel displaying events the student is actively registered for.
 */
public class RegistrationPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        page.add(UiUtil.pageHeader("My Event Registrations",
                "Events you have confirmed your attendance for.", null));
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
        List<Registration> regs = DummyData.getRegistrationsForEmail(mainFrame.getCurrentUser().getEmail());

        if (regs == null || regs.isEmpty()) {
            listHost.add(new EmptyState("📝", "No Registrations Found",
                    "You have not registered for any events yet. Discover events across colleges and sign up today!",
                    "Explore Events", () -> mainFrame.showCard(MainFrame.EVENTS)));
        } else {
            for (int i = 0; i < regs.size(); i++) {
                Registration reg = regs.get(i);
                final Event event = DummyData.findEventById(reg.getEventId());

                RoundedCard card = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
                card.setHoverable(true);
                card.setLayout(new BorderLayout(12, 10));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
                card.setAlignmentX(LEFT_ALIGNMENT);

                // Top
                JPanel top = new JPanel(new BorderLayout());
                top.setOpaque(false);

                JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                tags.setOpaque(false);
                tags.add(new PillBadge("CONFIRMED REGISTRATION",
                        new Color(236, 248, 240), AppTheme.SUCCESS, new Color(190, 230, 205)));
                if (event != null) {
                    tags.add(PillBadge.category(event.getCategory()));
                }
                top.add(tags, BorderLayout.WEST);

                JLabel regDateLbl = new JLabel("Registered: " + reg.getRegistrationDate().format(DATE_FMT));
                regDateLbl.setFont(AppTheme.SMALL);
                regDateLbl.setForeground(AppTheme.MUTED);
                top.add(regDateLbl, BorderLayout.EAST);
                card.add(top, BorderLayout.NORTH);

                // Mid
                JPanel mid = new JPanel();
                mid.setOpaque(false);
                mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

                JLabel title = new JLabel(reg.getEventName());
                title.setFont(AppTheme.HEADING);
                title.setForeground(AppTheme.NAVY);
                mid.add(title);
                mid.add(Box.createVerticalStrut(4));

                String venueInfo = event != null
                        ? event.getCollege() + "  ·  " + event.getVenue() + ", " + event.getLocation() + "  ·  📅 " + event.getDate().format(DATE_FMT)
                        : reg.getCollege();
                JLabel locLabel = new JLabel(venueInfo);
                locLabel.setFont(AppTheme.BODY);
                locLabel.setForeground(AppTheme.MUTED);
                mid.add(locLabel);

                card.add(mid, BorderLayout.CENTER);

                // South actions
                JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                actions.setOpaque(false);

                if (event != null) {
                    ModernButton viewBtn = new ModernButton("View Event Details", ModernButton.Variant.PRIMARY);
                    viewBtn.setCornerRadius(6);
                    viewBtn.addActionListener(e -> mainFrame.showEventDetails(event, MainFrame.REGISTRATIONS));
                    actions.add(viewBtn);
                }

                card.add(actions, BorderLayout.SOUTH);

                listHost.add(card);
                listHost.add(Box.createVerticalStrut(12));
            }
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
