package ui;

import model.Notice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Polished notice card with priority badges, clear announcement typography,
 * and college attribution.
 */
public class NoticeCardPanel extends RoundedCard {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public NoticeCardPanel(Notice notice) {
        super(12, AppTheme.CARD, AppTheme.BORDER);
        setHoverable(true);
        setLayout(new BorderLayout(10, 8));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        setAlignmentX(LEFT_ALIGNMENT);

        // TOP: Type Badge & Date
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        Color badgeBg = AppTheme.LAVENDER_SOFT;
        Color badgeFg = AppTheme.NAVY;
        if ("Important".equalsIgnoreCase(notice.getType())) {
            badgeBg = new Color(254, 238, 238);
            badgeFg = AppTheme.DANGER;
        } else if ("Event".equalsIgnoreCase(notice.getType())) {
            badgeBg = new Color(236, 244, 255);
            badgeFg = AppTheme.INDIGO;
        } else if ("Registration".equalsIgnoreCase(notice.getType())) {
            badgeBg = new Color(235, 248, 240);
            badgeFg = AppTheme.SUCCESS;
        } else if ("Club".equalsIgnoreCase(notice.getType())) {
            badgeBg = new Color(252, 244, 252);
            badgeFg = new Color(130, 60, 130);
        }

        PillBadge typeBadge = new PillBadge(notice.getType().toUpperCase() + " NOTICE", badgeBg, badgeFg);
        top.add(typeBadge, BorderLayout.WEST);

        JLabel dateLabel = new JLabel("📅  " + notice.getPostedDate().format(DATE));
        dateLabel.setFont(AppTheme.SMALL);
        dateLabel.setForeground(AppTheme.MUTED);
        top.add(dateLabel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // CENTER: Title & Description
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(notice.getTitle());
        title.setFont(AppTheme.HEADING);
        title.setForeground(AppTheme.NAVY);
        body.add(title);
        body.add(Box.createVerticalStrut(6));

        JLabel desc = new JLabel("<html><div style='width:680px;line-height:1.4;color:#1A233A;'>"
                + notice.getDescription() + "</div></html>");
        desc.setFont(AppTheme.BODY);
        body.add(desc);

        add(body, BorderLayout.CENTER);

        // SOUTH: College Origin
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottom.setOpaque(false);
        JLabel collegeLabel = new JLabel("🏛  Published by: " + notice.getCollege());
        collegeLabel.setFont(AppTheme.SMALL);
        collegeLabel.setForeground(AppTheme.MUTED);
        bottom.add(collegeLabel);

        add(bottom, BorderLayout.SOUTH);
    }
}
