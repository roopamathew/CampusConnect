package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Redesigned College Card with initials crest, campus details, and explore button.
 */
public class CollegeCardPanel extends RoundedCard {

    public CollegeCardPanel(final College college, final MainFrame mainFrame) {
        super(14, AppTheme.CARD, AppTheme.BORDER);
        setHoverable(true);
        setLayout(new BorderLayout(14, 10));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        setAlignmentX(LEFT_ALIGNMENT);

        // West: Uploaded College Image (Only if valid image exists)
        if (college.hasImage() && ImageUtil.isValidImage(college.getImagePath())) {
            javax.swing.JComponent colImg = ImageUtil.createImageViewer(college.getImagePath(), 56, 56, 10);
            if (colImg != null) {
                add(colImg, BorderLayout.WEST);
            }
        }

        // Center info
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nameRow.setOpaque(false);

        JLabel name = new JLabel(college.getCollegeName());
        name.setFont(AppTheme.HEADING);
        name.setForeground(AppTheme.NAVY);
        nameRow.add(name);

        int upcoming = DummyData.countUpcomingForCollege(college.getCollegeName());
        PillBadge countBadge = new PillBadge(upcoming + (upcoming == 1 ? " Event" : " Events"),
                new Color(236, 244, 255), AppTheme.INDIGO);
        nameRow.add(countBadge);
        center.add(nameRow);
        center.add(Box.createVerticalStrut(4));

        JLabel location = new JLabel("📍  " + college.getLocation() + "   ·   ✉  " + college.getEmail());
        location.setFont(AppTheme.SMALL);
        location.setForeground(AppTheme.MUTED);
        center.add(location);
        center.add(Box.createVerticalStrut(6));

        JLabel desc = new JLabel("<html><div style='width:580px;line-height:1.4;color:#1A233A;'>"
                + college.getDescription() + "</div></html>");
        desc.setFont(AppTheme.BODY);
        center.add(desc);

        add(center, BorderLayout.CENTER);

        // East action
        JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        east.setOpaque(false);
        ModernButton viewBtn = new ModernButton("View Campus Events →", ModernButton.Variant.PRIMARY);
        viewBtn.setCornerRadius(6);
        viewBtn.addActionListener(e -> mainFrame.showCollegeEvents(college.getCollegeName()));
        east.add(viewBtn);

        add(east, BorderLayout.EAST);
    }
}
