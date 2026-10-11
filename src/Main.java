import data.DummyData;
import ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * CampusConnect entry point. Loads in-memory dummy data, then opens login.
 */
public class Main {

    public static void main(String[] args) {
        DummyData.load();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception ignored) {
                    // Keep the default look and feel if Nimbus is unavailable.
                }
                UIManager.put("control", ui.AppTheme.BG);
                UIManager.put("Panel.background", ui.AppTheme.BG);
                UIManager.put("Viewport.background", ui.AppTheme.BG);
                UIManager.put("OptionPane.background", ui.AppTheme.BG);
                UIManager.put("nimbusBase", ui.AppTheme.NAVY);
                UIManager.put("nimbusBlueGrey", ui.AppTheme.LAVENDER);
                UIManager.put("nimbusFocus", ui.AppTheme.INDIGO);
                UIManager.put("nimbusSelectionBackground", ui.AppTheme.INDIGO);
                UIManager.put("text", ui.AppTheme.TEXT);
                UIManager.put("Label.foreground", ui.AppTheme.NAVY);
                UIManager.put("Table.background", ui.AppTheme.CARD);
                UIManager.put("Table.foreground", ui.AppTheme.TEXT);
                UIManager.put("Table.selectionBackground", ui.AppTheme.LAVENDER);
                UIManager.put("Table.selectionForeground", ui.AppTheme.NAVY);
                UIManager.put("Table.gridColor", ui.AppTheme.BORDER);
                UIManager.put("TableHeader.background", ui.AppTheme.LAVENDER_SOFT);
                UIManager.put("TableHeader.foreground", ui.AppTheme.NAVY);
                UIManager.put("TextField.background", ui.AppTheme.CARD);
                UIManager.put("PasswordField.background", ui.AppTheme.CARD);
                new LoginFrame().setVisible(true);
            }
        });
    }
}
