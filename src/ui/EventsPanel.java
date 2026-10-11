package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Redesigned Explore Events screen with multi-criteria filters, real-time search,
 * sorting, and results counter.
 */
public class EventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> categoryBox = new JComboBox<String>();
    private final JComboBox<String> collegeBox = new JComboBox<String>();
    private final JComboBox<String> locationBox = new JComboBox<String>();
    private final JComboBox<String> statusBox = new JComboBox<String>();
    private final JComboBox<String> sortBox = new JComboBox<String>(new String[] {
            "Date: Earliest First", "Date: Latest First", "Name: A-Z"
    });
    private final JLabel resultCountLabel = new JLabel();
    private boolean ready = false;

    public EventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Header
        page.add(UiUtil.pageHeader("Explore Events",
                "Discover hackathons, workshops, and competitions across all campuses.", null));
        page.add(Box.createVerticalStrut(16));

        // Filters Container Card
        page.add(buildFilterPanel());
        page.add(Box.createVerticalStrut(14));

        // Results counter row
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        countRow.setOpaque(false);
        countRow.setAlignmentX(LEFT_ALIGNMENT);
        resultCountLabel.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        resultCountLabel.setForeground(AppTheme.MUTED);
        countRow.add(resultCountLabel);
        page.add(countRow);
        page.add(Box.createVerticalStrut(12));

        // List host
        listHost = new JPanel();
        listHost.setLayout(new BoxLayout(listHost, BoxLayout.Y_AXIS));
        listHost.setOpaque(false);
        listHost.setAlignmentX(LEFT_ALIGNMENT);
        page.add(listHost);

        add(UiUtil.scroll(page), BorderLayout.CENTER);

        fillFilterBoxes();
        ready = true;
        refresh();
    }

    private JPanel buildFilterPanel() {
        RoundedCard card = new RoundedCard(14, AppTheme.CARD, AppTheme.BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Search Bar row
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        AppTheme.styleField(searchField);
        searchField.setPreferredSize(new Dimension(0, 38));
        searchField.putClientProperty("JTextField.placeholderText", "Search by event title, keyword, or college...");
        searchField.addActionListener(e -> refresh());

        ModernButton searchBtn = new ModernButton("Search", ModernButton.Variant.PRIMARY);
        searchBtn.setCornerRadius(6);
        searchBtn.addActionListener(e -> refresh());

        ModernButton resetBtn = new ModernButton("Reset", ModernButton.Variant.SECONDARY);
        resetBtn.setCornerRadius(6);
        resetBtn.addActionListener(e -> resetFilters());

        JPanel searchActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchActions.setOpaque(false);
        searchActions.add(searchBtn);
        searchActions.add(resetBtn);

        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchActions, BorderLayout.EAST);
        card.add(searchRow);
        card.add(Box.createVerticalStrut(12));

        // Dropdowns grid
        JPanel grid = new JPanel(new GridLayout(2, 5, 8, 4));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        grid.add(filterLabel("Category"));
        grid.add(filterLabel("College"));
        grid.add(filterLabel("Location"));
        grid.add(filterLabel("Status"));
        grid.add(filterLabel("Sort Order"));

        AppTheme.styleComboBox(categoryBox);
        AppTheme.styleComboBox(collegeBox);
        AppTheme.styleComboBox(locationBox);
        AppTheme.styleComboBox(statusBox);
        AppTheme.styleComboBox(sortBox);

        grid.add(categoryBox);
        grid.add(collegeBox);
        grid.add(locationBox);
        grid.add(statusBox);
        grid.add(sortBox);

        categoryBox.addActionListener(e -> { if (ready) refresh(); });
        collegeBox.addActionListener(e -> { if (ready) refresh(); });
        locationBox.addActionListener(e -> { if (ready) refresh(); });
        statusBox.addActionListener(e -> { if (ready) refresh(); });
        sortBox.addActionListener(e -> { if (ready) refresh(); });

        card.add(grid);
        return card;
    }

    private JLabel filterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL.deriveFont(Font.BOLD));
        label.setForeground(AppTheme.NAVY);
        return label;
    }

    private void fillFilterBoxes() {
        categoryBox.removeAllItems();
        categoryBox.addItem("All Categories");
        for (String c : DummyData.CATEGORIES) {
            categoryBox.addItem(c);
        }

        collegeBox.removeAllItems();
        collegeBox.addItem("All Colleges");
        for (String col : DummyData.getCollegeNames()) {
            collegeBox.addItem(col);
        }

        locationBox.removeAllItems();
        locationBox.addItem("All Locations");
        for (String loc : DummyData.getLocations()) {
            locationBox.addItem(loc);
        }

        statusBox.removeAllItems();
        statusBox.addItem("All Status");
        for (String st : DummyData.STATUSES) {
            statusBox.addItem(st);
        }
    }

    public void applySearch(String query) {
        searchField.setText(query == null ? "" : query);
        refresh();
    }

    private void resetFilters() {
        searchField.setText("");
        categoryBox.setSelectedIndex(0);
        collegeBox.setSelectedIndex(0);
        locationBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        sortBox.setSelectedIndex(0);
        refresh();
    }

    public void refresh() {
        List<Event> filtered = DummyData.filterEvents(
                searchField.getText(),
                String.valueOf(categoryBox.getSelectedItem()),
                String.valueOf(collegeBox.getSelectedItem()),
                String.valueOf(locationBox.getSelectedItem()),
                String.valueOf(statusBox.getSelectedItem()),
                String.valueOf(sortBox.getSelectedItem()));

        resultCountLabel.setText("Showing " + filtered.size() + (filtered.size() == 1 ? " event" : " events"));

        listHost.removeAll();
        if (filtered.isEmpty()) {
            listHost.add(new EmptyState("🔍", "No Events Found",
                    "Try adjusting your keyword, category, or college filters.",
                    "Reset All Filters", this::resetFilters));
        } else {
            listHost.add(EventCardPanel.listOf(filtered, mainFrame, MainFrame.EVENTS));
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
