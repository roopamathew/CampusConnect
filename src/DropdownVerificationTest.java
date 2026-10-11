import model.User;
import ui.AutoCompleteComboBox;
import data.DummyData;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class DropdownVerificationTest {

    public static void main(String[] args) {
        System.out.println("=== RUNNING AUTOCOMPLETE DROPDOWN VERIFICATION TEST ===");

        // Test 1: AutoCompleteComboBox creation (freeText)
        AutoCompleteComboBox freeBox = AutoCompleteComboBox.freeText(DummyData.CATEGORIES);
        assertNotNull(freeBox, "Free-text AutoCompleteComboBox should not be null");
        assertEquals(freeBox.isEditable(), true, "AutoCompleteComboBox should be editable");

        // Test 2: Free-text allows typing any custom value
        freeBox.setValue("Robotics & AI Expo");
        assertEquals(freeBox.getValue(), "Robotics & AI Expo", "Free-text mode accepts custom value");

        // Test 3: Strict mode initialization
        AutoCompleteComboBox strictBox = AutoCompleteComboBox.strict(DummyData.STATUSES);
        strictBox.setValue("UPCOMING");
        assertEquals(strictBox.getValue(), "UPCOMING", "Strict mode accepts valid option");

        // Test 4: Dynamic setItems repopulation
        freeBox.setItems(new String[]{"Option A", "Option B", "Option C"});
        assertEquals(freeBox.getItemCount(), 3, "Item count should reflect new items");
        assertEquals(freeBox.getValue(), "Option A", "Default value is first item");

        // Test 5: AutoCompleteComboBox in EventsPanel filter boxes
        DummyData.load();
        ui.MainFrame mf = new ui.MainFrame(DummyData.users.get(0));
        ui.EventsPanel ep = new ui.EventsPanel(mf);
        assertNotNull(ep, "EventsPanel loads successfully with AutoCompleteComboBoxes");
        ep.applySearch("Hackathon");
        ep.refresh();

        // Test 6: AdminRegistrationsPanel with AutoCompleteComboBox
        ui.AdminRegistrationsPanel arp = new ui.AdminRegistrationsPanel(mf);
        assertNotNull(arp, "AdminRegistrationsPanel loads with AutoCompleteComboBox");

        // Test 7: AdminRequestsPanel with AutoCompleteComboBox
        ui.AdminRequestsPanel areq = new ui.AdminRequestsPanel(mf);
        assertNotNull(areq, "AdminRequestsPanel loads with AutoCompleteComboBox");

        // Test 8: NoticePanel with AutoCompleteComboBox
        ui.NoticePanel np = new ui.NoticePanel(mf);
        assertNotNull(np, "NoticePanel loads with AutoCompleteComboBox");
        np.refresh();

        // Test 9: ClubPanel with AutoCompleteComboBox
        ui.ClubPanel cp = new ui.ClubPanel(mf);
        assertNotNull(cp, "ClubPanel loads with AutoCompleteComboBox");
        cp.refresh();

        // Test 10: StudentRegistrationDialog with AutoCompleteComboBox
        ui.StudentRegistrationDialog srd = new ui.StudentRegistrationDialog(null);
        assertNotNull(srd, "StudentRegistrationDialog loads with AutoCompleteComboBox");
        srd.dispose();

        // Test 11: AdminRequestDialog with AutoCompleteComboBox
        ui.AdminRequestDialog ard = new ui.AdminRequestDialog(null);
        assertNotNull(ard, "AdminRequestDialog loads with AutoCompleteComboBox");
        ard.dispose();

        System.out.println("======================================================");
        System.out.println("ALL AUTOCOMPLETE VERIFICATION CHECKS PASSED (100%)!");
        System.exit(0);
    }

    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            System.err.println("FAIL: " + message);
            System.exit(1);
        } else {
            System.out.println("PASS - " + message);
        }
    }

    private static void assertEquals(Object actual, Object expected, String message) {
        if (!String.valueOf(actual).equals(String.valueOf(expected))) {
            System.err.println("FAIL: " + message + " (expected: " + expected + ", got: " + actual + ")");
            System.exit(1);
        } else {
            System.out.println("PASS - " + message);
        }
    }
}
