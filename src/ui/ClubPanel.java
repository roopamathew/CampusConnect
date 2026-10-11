package ui;

import data.DummyData;
import model.Club;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Redesigned Clubs & Activities directory with category filter and search.
 */
public class ClubPanel extends JPanel {

    private final JPanel listHost;
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> categoryBox = new JComboBox<String>();
    private final JComboBox<String> collegeBox = new JComboBox<String>();

    public ClubPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Header
        page.add(UiUtil.pageHeader("Clubs & Student Societies",
                "Explore technical circles, cultural societies, and sports clubs across campuses.", null));
        page.add(Box.createVerticalStrut(16));

        // Filter Bar Card
        page.add(buildFilterBar());
        page.add(Box.createVerticalStrut(18));

        // List host
        listHost = new JPanel();
        listHost.setLayout(new BoxLayout(listHost, BoxLayout.Y_AXIS));
        listHost.setOpaque(false);
        listHost.setAlignmentX(LEFT_ALIGNMENT);
        page.add(listHost);

        add(UiUtil.scroll(page), BorderLayout.CENTER);
        initFilters();
        refresh();
    }

    private JPanel buildFilterBar() {
        RoundedCard bar = new RoundedCard(12, AppTheme.CARD, AppTheme.BORDER);
        bar.setLayout(new BorderLayout(12, 8));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        bar.setAlignmentX(LEFT_ALIGNMENT);

        AppTheme.styleField(searchField);
        searchField.setPreferredSize(new Dimension(240, 36));
        searchField.putClientProperty("JTextField.placeholderText", "Search clubs by name or keywords...");
        searchField.addActionListener(e -> refresh());

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filters.setOpaque(false);

        AppTheme.styleComboBox(categoryBox);
        AppTheme.styleComboBox(collegeBox);

        categoryBox.addActionListener(e -> refresh());
        collegeBox.addActionListener(e -> refresh());

        ModernButton filterBtn = new ModernButton("Filter", ModernButton.Variant.PRIMARY);
        filterBtn.setCornerRadius(6);
        filterBtn.addActionListener(e -> refresh());

        filters.add(new JLabel("Category:"));
        filters.add(categoryBox);
        filters.add(new JLabel("College:"));
        filters.add(collegeBox);
        filters.add(filterBtn);

        bar.add(searchField, BorderLayout.CENTER);
        bar.add(filters, BorderLayout.EAST);
        return bar;
    }

    private void initFilters() {
        categoryBox.removeAllItems();
        categoryBox.addItem("All Categories");
        for (String c : DummyData.CLUB_CATEGORIES) {
            categoryBox.addItem(c);
        }

        collegeBox.removeAllItems();
        collegeBox.addItem("All Colleges");
        for (String col : DummyData.getCollegeNames()) {
            collegeBox.addItem(col);
        }
    }

    public void refresh() {
        listHost.removeAll();
        String query = searchField.getText().trim().toLowerCase();
        String selCat = String.valueOf(categoryBox.getSelectedItem());
        String selCol = String.valueOf(collegeBox.getSelectedItem());

        List<Club> matched = new ArrayList<Club>();
        for (Club club : DummyData.clubs) {
            boolean matchesQuery = query.isEmpty()
                    || club.getClubName().toLowerCase().contains(query)
                    || club.getDescription().toLowerCase().contains(query);
            boolean matchesCat = "All Categories".equals(selCat) || club.getCategory().equalsIgnoreCase(selCat);
            boolean matchesCol = "All Colleges".equals(selCol) || club.getCollege().equalsIgnoreCase(selCol);

            if (matchesQuery && matchesCat && matchesCol) {
                matched.add(club);
            }
        }

        if (matched.isEmpty()) {
            listHost.add(new EmptyState("👥", "No Clubs Found",
                    "There are no student clubs matching your current filters.",
                    "Reset Filters", () -> {
                searchField.setText("");
                categoryBox.setSelectedIndex(0);
                collegeBox.setSelectedIndex(0);
                refresh();
            }));
        } else {
            for (int i = 0; i < matched.size(); i++) {
                listHost.add(new ClubCardPanel(matched.get(i)));
                listHost.add(Box.createVerticalStrut(12));
            }
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
