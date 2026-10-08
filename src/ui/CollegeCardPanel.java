package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CollegeCardPanel extends JPanel {

    public CollegeCardPanel(final College college, final MainFrame mainFrame) {
        setLayout(new BorderLayout(8, 8));
        AppTheme.styleCard(this);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        setAlignmentX(LEFT_ALIGNMENT);

        JLabel name = new JLabel(college.getCollegeName());
        name.setFont(AppTheme.HEADING);
        name.setForeground(AppTheme.NAVY);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(name);
        body.add(AppTheme.muted(college.getLocation()));
        body.add(Box.createVerticalStrut(6));
        JLabel desc = new JLabel("<html>" + college.getDescription() + "</html>");
        desc.setFont(AppTheme.BODY);
        desc.setForeground(AppTheme.TEXT);
        body.add(desc);
        body.add(Box.createVerticalStrut(6));
        int upcoming = DummyData.countUpcomingForCollege(college.getCollegeName());
        body.add(AppTheme.muted("Upcoming events: " + upcoming));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        JButton view = AppTheme.primaryButton("VIEW EVENTS");
        view.addActionListener(e -> mainFrame.showCollegeEvents(college.getCollegeName()));
        actions.add(view);

        add(body, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }
}
