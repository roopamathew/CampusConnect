import data.DummyData;
import model.AdminRequest;
import model.Event;
import model.User;
import ui.MainFrame;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ComprehensiveTest {

    public static void main(String[] args) {
        DummyData.load();
        int failures = 0;
        System.out.println("=== RUNNING EXTENDED VERIFICATION SUITE ===");

        // 1. Role Accounts Check
        User student = DummyData.findUser("student", "student123");
        User admin = DummyData.findUser("admin", "admin123");
        User superAdmin = DummyData.findUser("superadmin", "superadmin123");

        failures += check("Student user exists", student != null && "STUDENT".equals(student.getRole()));
        failures += check("Admin user exists", admin != null && "ADMIN".equals(admin.getRole()));
        failures += check("Super Admin user exists", superAdmin != null && "SUPER_ADMIN".equals(superAdmin.getRole()));

        // 2. Login with Email
        User studentByEmail = DummyData.findUser("rebecca@mbaselios.edu", "student123");
        User adminByEmail = DummyData.findUser("admin@campusconnect.edu", "admin123");
        User superAdminByEmail = DummyData.findUser("superadmin@campusconnect.edu", "superadmin123");
        failures += check("Student login via email", studentByEmail != null && studentByEmail.getUsername().equals("student"));
        failures += check("Admin login via email", adminByEmail != null && adminByEmail.getUsername().equals("admin"));
        failures += check("Super admin login via email", superAdminByEmail != null && superAdminByEmail.getUsername().equals("superadmin"));

        // 3. User Uniqueness Checks
        failures += check("Username taken check", DummyData.isUsernameTaken("student") && !DummyData.isUsernameTaken("newuniqueuser"));
        failures += check("Email taken check", DummyData.isEmailTaken("rebecca@mbaselios.edu") && !DummyData.isEmailTaken("unique@demo.edu"));

        // 4. Student Registration Workflow
        User newStudent = new User("aarav", "aarav123", "STUDENT", "Aarav Sharma",
                "aarav@mbaselios.edu", "Mar Baselios College", "Technology",
                "B.Tech Computer Science", "AI & Data Science", "Year 1, Sem 1", "2029", "Student", "+91 91234 56789");
        DummyData.users.add(newStudent);
        User verifiedStudent = DummyData.findUser("aarav", "aarav123");
        failures += check("New student registration & retrieval", verifiedStudent != null && "STUDENT".equals(verifiedStudent.getRole()));
        failures += check("New student profile course", verifiedStudent != null && "B.Tech Computer Science".equals(verifiedStudent.getCourse()));

        // 5. Admin Access Request & Approval Workflow
        int initialAdminCount = DummyData.getAdmins().size();
        AdminRequest req = new AdminRequest(DummyData.nextAdminRequestId(), "Dr. Sanjay Verma",
                "sanjay.v@pioneer.edu", "+91 99887 76655", "Pioneer Engineering College",
                "Head of Computing", "Manage inter-college coding events", LocalDate.now(), "PENDING");
        DummyData.adminRequests.add(req);
        failures += check("Pending request added", DummyData.hasPendingRequestForEmail("sanjay.v@pioneer.edu"));

        boolean approved = DummyData.approveAdminRequest(req, "sanjay123");
        failures += check("Request approved by Super Admin", approved && "APPROVED".equals(req.getStatus()));
        User provisionedAdmin = DummyData.findUser("sanjay.v", "sanjay123");
        if (provisionedAdmin == null) {
            provisionedAdmin = DummyData.findUser("sanjayv", "sanjay123");
        }
        if (provisionedAdmin == null) {
            provisionedAdmin = DummyData.findUser("sanjay.v@pioneer.edu", "sanjay123");
        }
        failures += check("Provisioned admin user created", provisionedAdmin != null && "ADMIN".equals(provisionedAdmin.getRole()));
        failures += check("Provisioned admin assigned college", provisionedAdmin != null && "Pioneer Engineering College".equals(provisionedAdmin.getCollege()));
        failures += check("Admin count incremented", DummyData.getAdmins().size() == initialAdminCount + 1);

        // 6. Admin Request Rejection Workflow
        AdminRequest rejectReq = new AdminRequest(DummyData.nextAdminRequestId(), "Fake Applicant",
                "fake@random.org", "+91 90000 00000", "Lakeside Business School",
                "None", "Invalid reason", LocalDate.now(), "PENDING");
        DummyData.adminRequests.add(rejectReq);
        boolean rejected = DummyData.rejectAdminRequest(rejectReq, "Ineligible applicant");
        failures += check("Request rejected", rejected && "REJECTED".equals(rejectReq.getStatus()));
        User fakeUser = DummyData.findUser("fake@random.org", "anypass");
        failures += check("Rejected request did NOT create account", fakeUser == null);

        // 7. Event Creator Attribution
        Event testEvent = new Event(DummyData.nextEventId(), "Hackathon 2026", "Test Desc", "Hackathon",
                admin.getCollege(), "Main Hall", "Campus", LocalDate.now().plusDays(10),
                LocalTime.of(10, 0), LocalTime.of(18, 0), LocalDate.now().plusDays(8),
                admin.getFullName(), admin.getEmail(), 100, 100, "Registration Open",
                admin.getFullName(), admin.getEmail(), admin.getDesignation());
        DummyData.events.add(testEvent);
        failures += check("Event creator name attribution", admin.getFullName().equals(testEvent.getCreatorName()));
        failures += check("Event creator email attribution", admin.getEmail().equals(testEvent.getCreatorEmail()));

        // 8. College-Scoped Filtering
        List<Event> mbEvents = DummyData.getEventsByCollege(admin.getCollege());
        failures += check("Admin college events query", mbEvents.contains(testEvent));

        // 9. Swing Frames and Role UI Initialisation
        try {
            ui.LoginFrame loginFrame = new ui.LoginFrame();
            failures += check("LoginFrame initialized", loginFrame.getTitle().contains("CampusConnect"));
            loginFrame.dispose();

            // Student MainFrame
            MainFrame sFrame = new MainFrame(student);
            sFrame.showCard(MainFrame.STUDENT_DASHBOARD);
            sFrame.showCard(MainFrame.EVENTS);
            sFrame.showCard(MainFrame.REGISTRATIONS);
            sFrame.showCard(MainFrame.BOOKMARKS);
            sFrame.showCard(MainFrame.NOTICES);
            sFrame.showCard(MainFrame.COLLEGES);
            sFrame.showCard(MainFrame.CLUBS);
            sFrame.showCard(MainFrame.PROFILE);
            sFrame.showEventDetails(testEvent, MainFrame.EVENTS);
            sFrame.showCollegeEvents("Horizon Institute of Technology");
            failures += check("Student screens loaded without errors", true);
            sFrame.dispose();

            // College Admin MainFrame
            MainFrame aFrame = new MainFrame(admin);
            aFrame.showCard(MainFrame.ADMIN_DASHBOARD);
            aFrame.showCard(MainFrame.MANAGE_EVENTS);
            aFrame.showCard(MainFrame.MANAGE_NOTICES);
            aFrame.showCard(MainFrame.MANAGE_CLUBS);
            aFrame.showCard(MainFrame.ADMIN_REGISTRATIONS);
            aFrame.showCard(MainFrame.PROFILE);
            failures += check("Admin screens loaded without errors", true);
            aFrame.dispose();

            // Super Admin MainFrame
            MainFrame saFrame = new MainFrame(superAdmin);
            saFrame.showCard(MainFrame.SUPER_ADMIN_DASHBOARD);
            saFrame.showCard(MainFrame.ADMIN_REQUESTS);
            saFrame.showCard(MainFrame.MANAGE_ADMINS);
            saFrame.showCard(MainFrame.MANAGE_COLLEGES);
            saFrame.showCard(MainFrame.MANAGE_EVENTS);
            saFrame.showCard(MainFrame.PROFILE);
            failures += check("Super Admin screens loaded without errors", true);
            saFrame.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            failures += check("Swing frame navigation error: " + ex.getMessage(), false);
        }

        System.out.println("===========================================");
        if (failures == 0) {
            System.out.println("ALL COMPREHENSIVE CHECKS PASSED SUCCESSFULLY!");
        } else {
            System.out.println("COMPREHENSIVE TEST FAILED: " + failures + " failures.");
            System.exit(1);
        }
    }

    private static int check(String name, boolean ok) {
        System.out.println((ok ? "PASS" : "FAIL") + " - " + name);
        return ok ? 0 : 1;
    }
}
