package ui;

import model.Club;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClubCardPanel extends JPanel {

    public ClubCardPanel(final Club club) {
        setLayout(new BorderLayout(8, 8));
        AppTheme.styleCard(this);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        setAlignmentX(LEFT_ALIGNMENT);

        JLabel name = new JLabel(club.getClubName());
        name.setFont(AppTheme.HEADING);
        name.setForeground(AppTheme.NAVY);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(name);
        body.add(AppTheme.muted(club.getCollege() + "  |  " + club.getCategory()));
        body.add(Box.createVerticalStrut(6));
        JLabel desc = new JLabel("<html>" + club.getDescription() + "</html>");
        desc.setFont(AppTheme.BODY);
        desc.setForeground(AppTheme.TEXT);
        body.add(desc);
        body.add(Box.createVerticalStrut(6));
        body.add(AppTheme.muted("Contact: " + club.getContactPerson()));
        body.add(AppTheme.muted(club.getContactEmail()));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        JButton view = AppTheme.primaryButton("VIEW DETAILS");
        view.addActionListener(e -> JOptionPane.showMessageDialog(this,
                club.getClubName() + "\n\n"
                        + club.getDescription() + "\n\n"
                        + "College: " + club.getCollege() + "\n"
                        + "Category: " + club.getCategory() + "\n"
                        + "Contact: " + club.getContactPerson() + "\n"
                        + club.getContactEmail(),
                "Club Details", JOptionPane.INFORMATION_MESSAGE));
        actions.add(view);

        add(body, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }
}
