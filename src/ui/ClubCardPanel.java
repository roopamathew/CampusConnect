package ui;

import model.Club;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Redesigned Club Card with category badge, college origin, description, and coordinator details.
 */
public class ClubCardPanel extends RoundedCard {

    public ClubCardPanel(final Club club) {
        super(12, AppTheme.CARD, AppTheme.BORDER);
        setHoverable(true);
        setLayout(new BorderLayout(12, 8));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        setAlignmentX(LEFT_ALIGNMENT);

        // North: Category & College Badges
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badges.setOpaque(false);
        badges.add(PillBadge.category(club.getCategory()));
        badges.add(new PillBadge("🏛  " + club.getCollege(), AppTheme.LAVENDER_SOFT, AppTheme.NAVY));
        top.add(badges, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // West: Uploaded Club Image (Only if valid image exists)
        if (club.hasImage() && ImageUtil.isValidImage(club.getImagePath())) {
            javax.swing.JComponent clubImg = ImageUtil.createImageViewer(club.getImagePath(), 64, 64, 10);
            if (clubImg != null) {
                add(clubImg, BorderLayout.WEST);
            }
        }

        // Center: Name & Description
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(club.getClubName());
        name.setFont(AppTheme.HEADING);
        name.setForeground(AppTheme.NAVY);
        center.add(name);
        center.add(Box.createVerticalStrut(4));

        JLabel desc = new JLabel("<html><div style='line-height:1.4;color:#1A233A;'>"
                + club.getDescription() + "</div></html>");
        desc.setFont(AppTheme.BODY);
        center.add(desc);
        center.add(Box.createVerticalStrut(6));

        JLabel contact = new JLabel("Lead: " + club.getContactPerson() + "  ·  " + club.getContactEmail());
        contact.setFont(AppTheme.SMALL);
        contact.setForeground(AppTheme.MUTED);
        center.add(contact);

        add(center, BorderLayout.CENTER);

        // East: Action Button
        JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        east.setOpaque(false);

        ModernButton viewBtn = new ModernButton("View Club Details", ModernButton.Variant.PRIMARY);
        viewBtn.setCornerRadius(6);
        viewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "<html><b>" + club.getClubName() + "</b><br><br>"
                            + "<b>College:</b> " + club.getCollege() + "<br>"
                            + "<b>Category:</b> " + club.getCategory() + "<br><br>"
                            + "<b>Description:</b><br>" + club.getDescription() + "<br><br>"
                            + "<b>Faculty / Student Lead:</b> " + club.getContactPerson() + "<br>"
                            + "<b>Contact Email:</b> " + club.getContactEmail() + "</html>",
                    "Club Information", JOptionPane.INFORMATION_MESSAGE);
        });
        east.add(viewBtn);

        add(east, BorderLayout.EAST);
    }
}
