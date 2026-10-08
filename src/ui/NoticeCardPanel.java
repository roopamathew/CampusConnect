package ui;

import model.Notice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NoticeCardPanel extends JPanel {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public NoticeCardPanel(Notice notice) {
        setLayout(new BorderLayout(6, 6));
        AppTheme.styleCard(this);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        setAlignmentX(LEFT_ALIGNMENT);

        JLabel type = new JLabel(notice.getType().toUpperCase() + " NOTICE");
        type.setFont(AppTheme.BOLD);
        type.setForeground(AppTheme.ACCENT);

        JLabel title = new JLabel(notice.getTitle());
        title.setFont(AppTheme.HEADING);
        title.setForeground(AppTheme.NAVY);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(title);
        body.add(Box.createVerticalStrut(4));
        JLabel desc = new JLabel("<html>" + notice.getDescription() + "</html>");
        desc.setFont(AppTheme.BODY);
        desc.setForeground(AppTheme.TEXT);
        body.add(desc);
        body.add(Box.createVerticalStrut(8));
        body.add(AppTheme.muted(notice.getCollege()));
        body.add(AppTheme.muted(notice.getPostedDate().format(DATE)));

        add(type, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }
}
