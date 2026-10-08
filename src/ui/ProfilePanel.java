package ui;

import model.User;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProfilePanel extends JPanel {

    public ProfilePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);
        User user = mainFrame.getCurrentUser();
        JPanel page = UiUtil.page("Profile", "Your CampusConnect student profile.");

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        AppTheme.styleCard(card);
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.add(row("Name", user.getFullName()));
        card.add(row("Username", user.getUsername()));
        String roleLabel = "STUDENT".equals(user.getRole()) ? "Student" : user.getRole();
        card.add(row("Role", roleLabel));
        card.add(row("College", user.getCollege()));
        card.add(row("Interest", user.getInterest()));
        page.add(card);

        add(UiUtil.scroll(page), BorderLayout.CENTER);
    }

    private JLabel row(String label, String value) {
        JLabel line = new JLabel(label + ":  " + value);
        line.setFont(AppTheme.BODY);
        line.setForeground(AppTheme.TEXT);
        return line;
    }
}
