package ui;

import data.DummyData;
import model.Event;
import model.Registration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RegistrationPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;

    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        JPanel page = UiUtil.page("My Registrations", "Events you have registered for.");
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
        if (regs.isEmpty()) {
            listHost.add(AppTheme.muted("You have not registered for any events yet."));
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM yyyy");
            for (int i = 0; i < regs.size(); i++) {
                Registration registration = regs.get(i);
                final Event event = DummyData.findEventById(registration.getEventId());
                JPanel card = new JPanel(new BorderLayout());
                AppTheme.styleCard(card);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
                card.setAlignmentX(LEFT_ALIGNMENT);

                JPanel body = new JPanel();
                body.setOpaque(false);
                body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
                JLabel name = new JLabel(registration.getEventName());
                name.setFont(AppTheme.HEADING);
                name.setForeground(AppTheme.NAVY);
                body.add(name);
                body.add(AppTheme.muted(registration.getCollege()));
                if (event != null) {
                    body.add(AppTheme.muted("Date: " + event.getDate().format(fmt)));
                    body.add(AppTheme.muted("Venue: " + event.getVenue()));
                    body.add(AppTheme.muted("Status: " + event.getStatus()));
                }

                JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                actions.setOpaque(false);
                JButton view = AppTheme.primaryButton("View Details");
                view.addActionListener(e -> {
                    if (event != null) {
                        mainFrame.showEventDetails(event, MainFrame.REGISTRATIONS);
                    }
                });
                actions.add(view);

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
