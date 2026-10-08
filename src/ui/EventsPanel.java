package ui;

import data.DummyData;
import model.Event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    private boolean ready;

    public EventsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG);

        JPanel page = UiUtil.page("Discover Events", "Search, filter and sort campus events.");
        page.add(buildFilters());
        page.add(Box.createVerticalStrut(12));

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

    private JPanel buildFilters() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(LEFT_ALIGNMENT);

        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setOpaque(false);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        searchField.setFont(AppTheme.BODY);
        JButton search = AppTheme.primaryButton("Search");
        search.addActionListener(e -> refresh());
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(search, BorderLayout.EAST);

        JPanel filters = new JPanel(new GridLayout(2, 5, 8, 8));
        filters.setOpaque(false);
        filters.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        filters.add(label("Category"));
        filters.add(label("College"));
        filters.add(label("Location"));
        filters.add(label("Status"));
        filters.add(label("Sort By"));
        filters.add(categoryBox);
        filters.add(collegeBox);
        filters.add(locationBox);
        filters.add(statusBox);
        filters.add(sortBox);

        categoryBox.addActionListener(e -> {
            if (ready) {
                refresh();
            }
        });
        collegeBox.addActionListener(e -> {
            if (ready) {
                refresh();
            }
        });
        locationBox.addActionListener(e -> {
            if (ready) {
                refresh();
            }
        });
        statusBox.addActionListener(e -> {
            if (ready) {
                refresh();
            }
        });
        sortBox.addActionListener(e -> {
            if (ready) {
                refresh();
            }
        });

        wrap.add(searchRow);
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(filters);
        return wrap;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL);
        label.setForeground(AppTheme.MUTED);
        return label;
    }

    private void fillFilterBoxes() {
        categoryBox.removeAllItems();
        categoryBox.addItem("All Categories");
        for (int i = 0; i < DummyData.CATEGORIES.length; i++) {
            categoryBox.addItem(DummyData.CATEGORIES[i]);
        }
        collegeBox.removeAllItems();
        collegeBox.addItem("All Colleges");
        List<String> colleges = DummyData.getCollegeNames();
        for (int i = 0; i < colleges.size(); i++) {
            collegeBox.addItem(colleges.get(i));
        }
        locationBox.removeAllItems();
        locationBox.addItem("All Locations");
        List<String> locations = DummyData.getLocations();
        for (int i = 0; i < locations.size(); i++) {
            locationBox.addItem(locations.get(i));
        }
        statusBox.removeAllItems();
        statusBox.addItem("All Status");
        for (int i = 0; i < DummyData.STATUSES.length; i++) {
            statusBox.addItem(DummyData.STATUSES[i]);
        }
    }

    public void applySearch(String query) {
        searchField.setText(query);
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
        listHost.removeAll();
        listHost.add(EventCardPanel.listOf(filtered, mainFrame, MainFrame.EVENTS));
        listHost.revalidate();
        listHost.repaint();
    }
}
