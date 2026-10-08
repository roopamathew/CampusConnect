import data.DummyData;
import model.College;
import model.Event;
import model.Notice;
import model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Command-line checks for in-memory flows. Not part of the Swing app.
 */
public class SmokeTest {

    public static void main(String[] args) {
        DummyData.load();
        int failures = 0;

        User student = DummyData.findUser("student", "student123");
        User admin = DummyData.findUser("admin", "admin123");
        User bad = DummyData.findUser("student", "wrong");
        failures += check("student login", student != null && "STUDENT".equals(student.getRole()));
        failures += check("demo student name", student != null && "Rebecca Zachariah".equals(student.getFullName()));
        failures += check("home college", student != null && "Mar Baselios College".equals(student.getCollege()));
        failures += check("admin login", admin != null && "ADMIN".equals(admin.getRole()));
        failures += check("invalid login", bad == null);

        failures += check("at least 10 events", DummyData.events.size() >= 10);
        failures += check("at least 5 colleges", DummyData.colleges.size() >= 5);

        List<Event> beyond = DummyData.getEventsBeyondCampus(student.getCollege());
        boolean allOther = true;
        for (int i = 0; i < beyond.size(); i++) {
            if (beyond.get(i).getCollege().equals(student.getCollege())) {
                allOther = false;
            }
        }
        failures += check("beyond campus filter", !beyond.isEmpty() && allOther);

        List<Event> recommended = DummyData.getRecommendedEvents(student.getInterest());
        failures += check("recommended events", !recommended.isEmpty());

        List<Event> hackathons = DummyData.filterEvents("hackathon", "All Categories", "All Colleges",
                "All Locations", "All Status", "Date: Earliest First");
        failures += check("search hackathon", !hackathons.isEmpty());

        List<Event> workshops = DummyData.filterEvents("", "Workshop", "All Colleges",
                "All Locations", "All Status", "Name: A-Z");
        failures += check("filter workshop", !workshops.isEmpty());

        Event extraCampus = null;
        for (int i = 0; i < beyond.size(); i++) {
            if (!DummyData.isRegistered(student.getEmail(), beyond.get(i).getEventId())) {
                extraCampus = beyond.get(i);
                break;
            }
        }
        failures += check("unregistered beyond event", extraCampus != null);
        boolean firstReg = extraCampus != null && DummyData.register(student, extraCampus);
        boolean dup = DummyData.register(student, extraCampus);
        failures += check("register", firstReg);
        failures += check("duplicate register blocked", !dup);

        DummyData.addBookmark(student.getEmail(), extraCampus.getEventId());
        failures += check("bookmark", DummyData.isBookmarked(student.getEmail(), extraCampus.getEventId()));
        DummyData.removeBookmark(student.getEmail(), extraCampus.getEventId());
        failures += check("remove bookmark", !DummyData.isBookmarked(student.getEmail(), extraCampus.getEventId()));

        Event created = new Event(DummyData.nextEventId(), "Smoke Test Event", "Demo", "Other",
                DummyData.colleges.get(0).getCollegeName(), "Hall", "Kochi, Kerala",
                LocalDate.of(2026, 12, 1), LocalTime.of(9, 0), LocalTime.of(12, 0),
                LocalDate.of(2026, 11, 20), "Admin", "admin@campusconnect.edu",
                10, 10, "Registration Open");
        DummyData.events.add(created);
        created.setEventName("Smoke Test Event Updated");
        failures += check("admin add/edit event", "Smoke Test Event Updated".equals(created.getEventName()));
        DummyData.deleteEvent(created);
        failures += check("admin delete event", DummyData.findEventById(created.getEventId()) == null);

        DummyData.notices.add(new Notice(DummyData.nextNoticeId(), "Smoke Notice", "Body",
                student.getCollege(), LocalDate.now(), "General"));
        failures += check("add notice", DummyData.notices.size() >= 6);

        DummyData.colleges.add(new College(DummyData.nextCollegeId(), "Smoke College",
                "Goa", "smoke@demo.edu", "Temporary"));
        failures += check("add college", DummyData.colleges.size() >= 6);

        try {
            DummyData.load();
            ui.LoginFrame login = new ui.LoginFrame();
            failures += check("login frame", login.getTitle().contains("CampusConnect"));
            login.dispose();
            ui.MainFrame studentFrame = new ui.MainFrame(student);
            failures += check("student main frame", studentFrame.getTitle().contains("Discover Beyond Your Campus"));
            studentFrame.showCard(ui.MainFrame.PROFILE);
            studentFrame.showCard(ui.MainFrame.EVENTS);
            studentFrame.searchFromDashboard("hackathon");
            studentFrame.dispose();
            ui.MainFrame adminFrame = new ui.MainFrame(admin);
            adminFrame.showCard(ui.MainFrame.MANAGE_EVENTS);
            adminFrame.showCard(ui.MainFrame.ADMIN_REGISTRATIONS);
            failures += check("admin main frame", adminFrame.getTitle().contains("CampusConnect"));
            adminFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            failures += check("swing frames", false);
        }

        if (failures == 0) {
            System.out.println("SMOKE TEST PASSED");
        } else {
            System.out.println("SMOKE TEST FAILED: " + failures);
            System.exit(1);
        }
    }

    private static int check(String name, boolean ok) {
        System.out.println((ok ? "PASS" : "FAIL") + " - " + name);
        return ok ? 0 : 1;
    }
}
