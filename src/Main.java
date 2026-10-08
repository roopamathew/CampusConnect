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
                new LoginFrame().setVisible(true);
            }
        });
    }
}
