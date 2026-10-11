package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
 * Redesigned Explore Events screen with multi-criteria filters, real-time search,
 * sorting, and results counter.
 */
public class EventsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel listHost;
    private final JTextField searchField = new JTextField();
    private final AutoCompleteComboBox categoryBox = AutoCompleteComboBox.freeText(new String[]{"All Categories"});
    private final AutoCompleteComboBox collegeBox = AutoCompleteComboBox.freeText(new String[]{"All Colleges"});
    private final AutoCompleteComboBox locationBox = AutoCompleteComboBox.freeText(new String[]{"All Locations"});
    private final AutoCompleteComboBox statusBox = AutoCompleteComboBox.freeText(new String[]{"All Status"});
    private final AutoCompleteComboBox sortBox = AutoCompleteComboBox.strict(new String[] {
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Search Bar row
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

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

        // Dropdowns grid: 2 rows (labels + dropdowns), 5 columns
        JPanel grid = new JPanel(new java.awt.GridLayout(2, 5, 8, 4));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

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
        List<String> cats = new ArrayList<String>();
        cats.add("All Categories");
        for (String c : DummyData.CATEGORIES) cats.add(c);
        categoryBox.setItems(cats.toArray(new String[0]));

        List<String> cols = new ArrayList<String>();
        cols.add("All Colleges");
        for (String col : DummyData.getCollegeNames()) cols.add(col);
        collegeBox.setItems(cols.toArray(new String[0]));

        List<String> locs = new ArrayList<String>();
        locs.add("All Locations");
        for (String loc : DummyData.getLocations()) locs.add(loc);
        locationBox.setItems(locs.toArray(new String[0]));

        List<String> stats = new ArrayList<String>();
        stats.add("All Status");
        for (String st : DummyData.STATUSES) stats.add(st);
        statusBox.setItems(stats.toArray(new String[0]));
    }

    public void applySearch(String query) {
        searchField.setText(query == null ? "" : query);
        refresh();
    }

    private void resetFilters() {
        searchField.setText("");
        categoryBox.setValue("All Categories");
        collegeBox.setValue("All Colleges");
        locationBox.setValue("All Locations");
        statusBox.setValue("All Status");
        sortBox.setValue("Date: Earliest First");
        refresh();
    }

    public void refresh() {
        List<Event> filtered = DummyData.filterEvents(
                searchField.getText(),
                categoryBox.getValue(),
                collegeBox.getValue(),
                locationBox.getValue(),
                statusBox.getValue(),
                sortBox.getValue());

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
