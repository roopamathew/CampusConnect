
import data.DummyData;
import ui.AppTheme;
import ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * CampusConnect entry point.
 * Loads in-memory dummy data and opens the login screen in light mode.
 */
public class Main {

    public static void main(String[] args) {
        DummyData.load();

        // Ensure light mode is active before any UI is constructed
        AppTheme.setDarkMode(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                    );
                } catch (Exception ignored) {
                    // Keep the default look and feel if Nimbus is unavailable.
                }

                // Apply light-mode UIManager defaults
                UIManager.put("control",              AppTheme.BG);
                UIManager.put("Panel.background",     AppTheme.BG);
                UIManager.put("Viewport.background",  AppTheme.BG);
                UIManager.put("OptionPane.background", AppTheme.CARD);
                UIManager.put("OptionPane.messageForeground", AppTheme.TEXT);

                UIManager.put("nimbusBase",               AppTheme.NAVY);
                UIManager.put("nimbusBlueGrey",           AppTheme.LIGHT_LAVENDER_SOFT);
                UIManager.put("nimbusFocus",              AppTheme.PRIMARY);
                UIManager.put("nimbusSelectionBackground", AppTheme.PRIMARY);

                UIManager.put("text",             AppTheme.TEXT);
                UIManager.put("Label.foreground", AppTheme.TEXT);

                UIManager.put("Table.background",         AppTheme.CARD);
                UIManager.put("Table.foreground",         AppTheme.TEXT);
                UIManager.put("Table.selectionBackground", AppTheme.LAVENDER_SOFT);
                UIManager.put("Table.selectionForeground", AppTheme.NAVY);
                UIManager.put("Table.gridColor",          AppTheme.BORDER);

                UIManager.put("TableHeader.background",   AppTheme.LAVENDER_SOFT);
                UIManager.put("TableHeader.foreground",   AppTheme.NAVY);

                UIManager.put("TextField.background",  AppTheme.CARD);
                UIManager.put("TextField.foreground",  AppTheme.TEXT);
                UIManager.put("PasswordField.background", AppTheme.CARD);
                UIManager.put("PasswordField.foreground", AppTheme.TEXT);

                UIManager.put("ScrollPane.background", AppTheme.BG);
                UIManager.put("ScrollBar.background",  AppTheme.CARD_ELEVATED);

                new LoginFrame().setVisible(true);
            }
        });
    }
}
