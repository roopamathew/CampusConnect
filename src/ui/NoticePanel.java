package ui;

import data.DummyData;
import model.Notice;

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
 * Redesigned Notice Board screen with category filter, search, and notice feed.
 */
public class NoticePanel extends JPanel {

    private final JPanel listHost;
    private final JTextField searchField = new JTextField();
    private final AutoCompleteComboBox typeFilter = AutoCompleteComboBox.strict(new String[]{"All Types"});
    private final AutoCompleteComboBox collegeFilter = AutoCompleteComboBox.freeText(new String[]{"All Colleges"});

    public NoticePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(AppTheme.BG);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Header
        page.add(UiUtil.pageHeader("Notice Board",
                "Official announcements, event updates, and campus bulletins.", null));
        page.add(Box.createVerticalStrut(16));

        // Filters
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
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        bar.setAlignmentX(LEFT_ALIGNMENT);

        AppTheme.styleField(searchField);
        searchField.setPreferredSize(new Dimension(240, 36));
        searchField.putClientProperty("JTextField.placeholderText", "Search announcements...");
        searchField.addActionListener(e -> refresh());

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filters.setOpaque(false);

        AppTheme.styleComboBox(typeFilter);
        AppTheme.styleComboBox(collegeFilter);

        typeFilter.addActionListener(e -> refresh());
        collegeFilter.addActionListener(e -> refresh());

        ModernButton searchBtn = new ModernButton("Filter", ModernButton.Variant.PRIMARY);
        searchBtn.setCornerRadius(6);
        searchBtn.addActionListener(e -> refresh());

        filters.add(new JLabel("Type:"));
        filters.add(typeFilter);
        filters.add(new JLabel("College:"));
        filters.add(collegeFilter);
        filters.add(searchBtn);

        bar.add(searchField, BorderLayout.CENTER);
        bar.add(filters, BorderLayout.EAST);
        return bar;
    }

    private void initFilters() {
        List<String> types = new ArrayList<String>();
        types.add("All Types");
        for (String t : DummyData.NOTICE_TYPES) {
            types.add(t);
        }
        typeFilter.setItems(types.toArray(new String[0]));

        List<String> colleges = new ArrayList<String>();
        colleges.add("All Colleges");
        for (String c : DummyData.getCollegeNames()) {
            colleges.add(c);
        }
        collegeFilter.setItems(colleges.toArray(new String[0]));
    }

    public void refresh() {
        listHost.removeAll();

        String query = searchField.getText().trim().toLowerCase();
        String selType = typeFilter.getValue();
        String selCollege = collegeFilter.getValue();

        List<Notice> filtered = new ArrayList<Notice>();
        for (Notice notice : DummyData.notices) {
            boolean matchesQuery = query.isEmpty()
                    || notice.getTitle().toLowerCase().contains(query)
                    || notice.getDescription().toLowerCase().contains(query);
            boolean matchesType = "All Types".equalsIgnoreCase(selType) || notice.getType().equalsIgnoreCase(selType);
            boolean matchesCollege = "All Colleges".equalsIgnoreCase(selCollege) || notice.getCollege().equalsIgnoreCase(selCollege);

            if (matchesQuery && matchesType && matchesCollege) {
                filtered.add(notice);
            }
        }

        if (filtered.isEmpty()) {
            listHost.add(new EmptyState("📢", "No Announcements Found",
                    "There are no notices matching your current search criteria.",
                    "Reset Filters", () -> {
                searchField.setText("");
                typeFilter.setValue("All Types");
                collegeFilter.setValue("All Colleges");
                refresh();
            }));
        } else {
            for (int i = 0; i < filtered.size(); i++) {
                listHost.add(new NoticeCardPanel(filtered.get(i)));
                listHost.add(Box.createVerticalStrut(12));
            }
        }

        listHost.revalidate();
        listHost.repaint();
    }
}
