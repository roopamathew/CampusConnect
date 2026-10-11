package ui;

import data.DummyData;
import model.College;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Redesigned Explore Colleges screen with campus search and directory listing.
 */
public class CollegePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;
    private final JTextField searchField = new JTextField();

    public CollegePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Header
        page.add(UiUtil.pageHeader("Explore Partner Colleges",
                "Discover partner campuses and their host departments across regions.", null));
        page.add(Box.createVerticalStrut(16));

        // Search Bar
        RoundedCard searchCard = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        searchCard.setLayout(new BorderLayout(10, 0));
        searchCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        searchCard.setAlignmentX(LEFT_ALIGNMENT);

        AppTheme.styleField(searchField);
        searchField.putClientProperty("JTextField.placeholderText", "Search colleges by name, city, or state...");
        searchField.addActionListener(e -> refresh());

        ModernButton searchBtn = new ModernButton("Search Colleges", ModernButton.Variant.PRIMARY);
        searchBtn.setCornerRadius(6);
        searchBtn.addActionListener(e -> refresh());

        ModernButton resetBtn = new ModernButton("Reset", ModernButton.Variant.SECONDARY);
        resetBtn.setCornerRadius(6);
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            refresh();
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);
        actions.add(searchBtn);
        actions.add(resetBtn);

        searchCard.add(searchField, BorderLayout.CENTER);
        searchCard.add(actions, BorderLayout.EAST);

        page.add(searchCard);
        page.add(Box.createVerticalStrut(18));

        // List host
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
        String query = searchField.getText().trim().toLowerCase();

        List<College> matched = new ArrayList<College>();
        for (College college : DummyData.colleges) {
            if (query.isEmpty()
                    || college.getCollegeName().toLowerCase().contains(query)
                    || college.getLocation().toLowerCase().contains(query)
                    || college.getDescription().toLowerCase().contains(query)) {
                matched.add(college);
            }
        }

        if (matched.isEmpty()) {
            listHost.add(new EmptyState("🏛", "No Colleges Found",
                    "No registered campuses match your search query.",
                    "View All Colleges", () -> {
                searchField.setText("");
                refresh();
            }));
        } else {
            for (int i = 0; i < matched.size(); i++) {
                listHost.add(new CollegeCardPanel(matched.get(i), mainFrame));
                listHost.add(Box.createVerticalStrut(12));
            }
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
