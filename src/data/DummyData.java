package data;

import model.Club;
import model.College;
import model.Event;
import model.Notice;
import model.Registration;
import model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory sample data. Later this class can be replaced with database calls.
 */
public class DummyData {

    public static final List<Event> events = new ArrayList<Event>();
    public static final List<College> colleges = new ArrayList<College>();
    public static final List<Notice> notices = new ArrayList<Notice>();
    public static final List<Club> clubs = new ArrayList<Club>();
    public static final List<User> users = new ArrayList<User>();
    public static final List<Registration> registrations = new ArrayList<Registration>();
    public static final Map<String, List<Integer>> bookmarksByEmail = new HashMap<String, List<Integer>>();

    private static boolean loaded = false;

    public static final String[] CATEGORIES = {
            "Hackathon", "Workshop", "Seminar", "Competition", "Cultural",
            "Sports", "Technical", "Career", "Club Activity", "Other"
    };

    public static final String[] STATUSES = {
            "Registration Open", "Registration Closed", "Upcoming", "Completed"
    };

    public static final String[] NOTICE_TYPES = {
            "Important", "Event", "Club", "Registration", "General"
    };

    public static final String[] CLUB_CATEGORIES = {
            "Technical", "Cultural", "Sports", "Finance",
            "Entrepreneurship", "Arts", "Social Service", "Other"
    };

    public static void load() {
        if (loaded) {
            return;
        }
        loaded = true;

        colleges.add(new College(1, "Pioneer Engineering College", "Kochi, Kerala",
                "info@pioneer.edu", "A technology-focused campus known for coding clubs and inter-college hackathons."));
        colleges.add(new College(2, "Horizon Institute of Technology", "Bengaluru, Karnataka",
                "hello@horizontech.edu", "Innovation hub for AI, robotics and startup culture."));
        colleges.add(new College(3, "Meridian Arts & Science College", "Chennai, Tamil Nadu",
                "office@meridian.edu", "Vibrant cultural campus with fests, seminars and student communities."));
        colleges.add(new College(4, "Lakeside Business School", "Pune, Maharashtra",
                "contact@lakesidebschool.edu", "Business school organising finance cases, pitch nights and workshops."));
        colleges.add(new College(5, "Summit College of Design", "Hyderabad, Telangana",
                "studio@summitdesign.edu", "Creative campus for design, photography and visual arts."));
        colleges.add(new College(6, "Mar Baselios College", "Thiruvananthapuram, Kerala",
                "info@mbaselios.edu", "The demo student's home campus for engineering, technology and student clubs."));

        String pioneer = "Pioneer Engineering College";
        String horizon = "Horizon Institute of Technology";
        String meridian = "Meridian Arts & Science College";
        String lakeside = "Lakeside Business School";
        String summit = "Summit College of Design";
        String marBaselios = "Mar Baselios College";

        events.add(new Event(1, "Inter College Hackathon",
                "A 24-hour inter-college hackathon on campus tech problems. Teams from any college can join.",
                "Hackathon", horizon, "Innovation Lab", "Bengaluru, Karnataka",
                LocalDate.of(2026, 10, 18), LocalTime.of(9, 0), LocalTime.of(17, 0),
                LocalDate.of(2026, 10, 14), "Ananya Rao", "ananya@horizontech.edu",
                120, 84, "Registration Open"));

        events.add(new Event(2, "AI Innovation Workshop",
                "Hands-on workshop covering practical AI tools for student projects.",
                "Workshop", pioneer, "Seminar Hall A", "Kochi, Kerala",
                LocalDate.of(2026, 10, 10), LocalTime.of(10, 0), LocalTime.of(16, 0),
                LocalDate.of(2026, 10, 8), "Rahul Menon", "rahul@pioneer.edu",
                80, 22, "Registration Open"));

        events.add(new Event(3, "Finance Case Competition",
                "Solve a live business case with peers from other campuses. Open to all streams.",
                "Competition", lakeside, "Case Arena", "Pune, Maharashtra",
                LocalDate.of(2026, 10, 25), LocalTime.of(9, 30), LocalTime.of(16, 30),
                LocalDate.of(2026, 10, 20), "Meera Iyer", "meera@lakesidebschool.edu",
                60, 41, "Registration Open"));

        events.add(new Event(4, "Startup Pitch Challenge",
                "Pitch your campus startup idea to a panel of faculty and alumni mentors.",
                "Career", horizon, "Auditorium", "Bengaluru, Karnataka",
                LocalDate.of(2026, 11, 2), LocalTime.of(11, 0), LocalTime.of(17, 0),
                LocalDate.of(2026, 10, 28), "Kiran Shah", "kiran@horizontech.edu",
                40, 18, "Upcoming"));

        events.add(new Event(5, "Cultural Fest",
                "Music, dance and theatre showcase inviting performers from neighbouring colleges.",
                "Cultural", meridian, "Open Air Theatre", "Chennai, Tamil Nadu",
                LocalDate.of(2026, 11, 8), LocalTime.of(16, 0), LocalTime.of(21, 0),
                LocalDate.of(2026, 11, 1), "Divya Nair", "divya@meridian.edu",
                300, 210, "Registration Open"));

        events.add(new Event(6, "Inter College Football Tournament",
                "Knockout football tournament for mixed college teams.",
                "Sports", pioneer, "Main Ground", "Kochi, Kerala",
                LocalDate.of(2026, 10, 22), LocalTime.of(8, 0), LocalTime.of(18, 0),
                LocalDate.of(2026, 10, 16), "Arjun Das", "arjun@pioneer.edu",
                16, 7, "Registration Open"));

        events.add(new Event(7, "Entrepreneurship Workshop",
                "Learn how to validate an idea, talk to customers and build a simple business model.",
                "Workshop", lakeside, "Studio 2", "Pune, Maharashtra",
                LocalDate.of(2026, 10, 12), LocalTime.of(14, 0), LocalTime.of(17, 0),
                LocalDate.of(2026, 10, 9), "Sneha Kulkarni", "sneha@lakesidebschool.edu",
                50, 19, "Registration Open"));

        events.add(new Event(8, "Coding Challenge",
                "Timed programming contest with beginner and advanced tracks.",
                "Technical", pioneer, "Computer Lab 3", "Kochi, Kerala",
                LocalDate.of(2026, 10, 14), LocalTime.of(13, 0), LocalTime.of(16, 0),
                LocalDate.of(2026, 10, 11), "Neel Joseph", "neel@pioneer.edu",
                100, 55, "Registration Open"));

        events.add(new Event(9, "Photography Competition",
                "Submit campus-life photographs. Theme: Discover Beyond Your Campus.",
                "Competition", summit, "Design Gallery", "Hyderabad, Telangana",
                LocalDate.of(2026, 10, 30), LocalTime.of(10, 0), LocalTime.of(15, 0),
                LocalDate.of(2026, 10, 24), "Tara Reddy", "tara@summitdesign.edu",
                90, 63, "Registration Open"));

        events.add(new Event(10, "Career Development Seminar",
                "Resume, internships and interview skills for final-year students.",
                "Seminar", meridian, "Lecture Theatre 1", "Chennai, Tamil Nadu",
                LocalDate.of(2026, 9, 20), LocalTime.of(10, 0), LocalTime.of(13, 0),
                LocalDate.of(2026, 9, 15), "Vikram Pillai", "vikram@meridian.edu",
                150, 0, "Completed"));

        events.add(new Event(11, "Campus Tech Meetup",
                "Monthly meetup for coding clubs, project demos and peer mentoring on campus.",
                "Technical", marBaselios, "Seminar Hall", "Thiruvananthapuram, Kerala",
                LocalDate.of(2026, 10, 16), LocalTime.of(15, 0), LocalTime.of(17, 30),
                LocalDate.of(2026, 10, 14), "Rebecca Zachariah", "rebecca@mbaselios.edu",
                80, 48, "Registration Open"));

        events.add(new Event(12, "Department Orientation Workshop",
                "Hands-on workshop for first-year students covering campus labs and student projects.",
                "Workshop", marBaselios, "Computer Lab 1", "Thiruvananthapuram, Kerala",
                LocalDate.of(2026, 10, 20), LocalTime.of(10, 0), LocalTime.of(13, 0),
                LocalDate.of(2026, 10, 17), "Faculty Coordinator", "events@mbaselios.edu",
                100, 72, "Registration Open"));

        notices.add(new Notice(1, "Hackathon registration deadline extended",
                "Horizon Institute has extended the Inter College Hackathon deadline by two days.",
                horizon, LocalDate.of(2026, 10, 7), "Important"));
        notices.add(new Notice(2, "Football team trials this week",
                "Pioneer Engineering College football trials will be held on the main ground.",
                pioneer, LocalDate.of(2026, 10, 6), "Event"));
        notices.add(new Notice(3, "New coding club members welcome",
                "CodeForge Club is accepting members from all years. No prior contest experience needed.",
                pioneer, LocalDate.of(2026, 10, 5), "Club"));
        notices.add(new Notice(4, "Finance case registrations open",
                "Lakeside Business School is accepting teams of three for the Finance Case Competition.",
                lakeside, LocalDate.of(2026, 10, 4), "Registration"));
        notices.add(new Notice(5, "Library hours during cultural week",
                "Meridian library will stay open until 8 PM during Cultural Fest week.",
                meridian, LocalDate.of(2026, 10, 3), "General"));

        clubs.add(new Club(1, "CodeForge Club", pioneer, "Technical",
                "Weekly coding practice, hackathon teams and peer mentoring.",
                "Neel Joseph", "codeforge@pioneer.edu"));
        clubs.add(new Club(2, "Horizon AI Circle", horizon, "Technical",
                "Student group exploring applied AI projects and paper discussions.",
                "Ananya Rao", "ai@horizontech.edu"));
        clubs.add(new Club(3, "Meridian Cultural Society", meridian, "Cultural",
                "Dance, music and theatre collective that hosts the annual cultural fest.",
                "Divya Nair", "culture@meridian.edu"));
        clubs.add(new Club(4, "Lakeside Finance Forum", lakeside, "Finance",
                "Case practice, markets club and guest sessions with alumni.",
                "Meera Iyer", "finance@lakesidebschool.edu"));
        clubs.add(new Club(5, "Pioneer Kickoff FC", pioneer, "Sports",
                "Football club organising inter-college matches and fitness sessions.",
                "Arjun Das", "football@pioneer.edu"));
        clubs.add(new Club(6, "Summit Lens Club", summit, "Arts",
                "Photography walks, editing workshops and campus exhibitions.",
                "Tara Reddy", "lens@summitdesign.edu"));

        users.add(new User("student", "student123", "STUDENT", "Rebecca Zachariah",
                "rebecca@mbaselios.edu", marBaselios, "Technology"));
        users.add(new User("admin", "admin123", "ADMIN", "Campus Admin",
                "admin@campusconnect.edu", marBaselios, "General"));
        users.add(new User("rohan", "rohan123", "STUDENT", "Rohan Patel",
                "rohan@horizontech.edu", horizon, "Technology"));
        users.add(new User("nisha", "nisha123", "STUDENT", "Nisha Verma",
                "nisha@lakesidebschool.edu", lakeside, "Finance"));
        users.add(new User("leo", "leo123", "STUDENT", "Leo Fernandez",
                "leo@meridian.edu", meridian, "Cultural"));
        users.add(new User("maya", "maya123", "STUDENT", "Maya Krishnan",
                "maya@summitdesign.edu", summit, "Arts"));

        registrations.add(new Registration("Rebecca Zachariah", "rebecca@mbaselios.edu", 1,
                "Inter College Hackathon", horizon, LocalDate.of(2026, 10, 2)));
        registrations.add(new Registration("Rebecca Zachariah", "rebecca@mbaselios.edu", 8,
                "Coding Challenge", pioneer, LocalDate.of(2026, 10, 3)));
        registrations.add(new Registration("Rohan Patel", "rohan@horizontech.edu", 1,
                "Inter College Hackathon", horizon, LocalDate.of(2026, 10, 1)));
        registrations.add(new Registration("Nisha Verma", "nisha@lakesidebschool.edu", 3,
                "Finance Case Competition", lakeside, LocalDate.of(2026, 10, 4)));
        registrations.add(new Registration("Leo Fernandez", "leo@meridian.edu", 5,
                "Cultural Fest", meridian, LocalDate.of(2026, 10, 5)));

        List<Integer> rebeccaBookmarks = new ArrayList<Integer>();
        rebeccaBookmarks.add(3);
        rebeccaBookmarks.add(7);
        bookmarksByEmail.put("rebecca@mbaselios.edu", rebeccaBookmarks);
    }

    public static User findUser(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static Event findEventById(int eventId) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == eventId) {
                return events.get(i);
            }
        }
        return null;
    }

    public static int nextEventId() {
        int max = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() > max) {
                max = events.get(i).getEventId();
            }
        }
        return max + 1;
    }

    public static int nextCollegeId() {
        int max = 0;
        for (int i = 0; i < colleges.size(); i++) {
            if (colleges.get(i).getCollegeId() > max) {
                max = colleges.get(i).getCollegeId();
            }
        }
        return max + 1;
    }

    public static int nextNoticeId() {
        int max = 0;
        for (int i = 0; i < notices.size(); i++) {
            if (notices.get(i).getNoticeId() > max) {
                max = notices.get(i).getNoticeId();
            }
        }
        return max + 1;
    }

    public static int nextClubId() {
        int max = 0;
        for (int i = 0; i < clubs.size(); i++) {
            if (clubs.get(i).getClubId() > max) {
                max = clubs.get(i).getClubId();
            }
        }
        return max + 1;
    }

    public static List<Event> getUpcomingEvents() {
        List<Event> result = new ArrayList<Event>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (!event.getDate().isBefore(today) && !"Completed".equals(event.getStatus())) {
                result.add(event);
            }
        }
        return result;
    }

    public static int countUpcomingEvents() {
        return getUpcomingEvents().size();
    }

    /**
     * Events from colleges other than the student's own campus.
     */
    public static List<Event> getEventsBeyondCampus(String studentCollege) {
        List<Event> result = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (!event.getCollege().equals(studentCollege) && !"Completed".equals(event.getStatus())) {
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Simple category matching based on the student's interest. No AI is used.
     */
    public static List<Event> getRecommendedEvents(String interest) {
        List<Event> result = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if ("Completed".equals(event.getStatus())) {
                continue;
            }
            if (matchesInterest(event, interest)) {
                result.add(event);
            }
        }
        return result;
    }

    private static boolean matchesInterest(Event event, String interest) {
        if (interest == null) {
            return false;
        }
        String category = event.getCategory();
        if ("Technology".equalsIgnoreCase(interest)) {
            return "Hackathon".equals(category) || "Technical".equals(category)
                    || "Workshop".equals(category) || "Competition".equals(category);
        }
        if ("Finance".equalsIgnoreCase(interest)) {
            return "Competition".equals(category) || "Career".equals(category) || "Seminar".equals(category);
        }
        if ("Cultural".equalsIgnoreCase(interest) || "Arts".equalsIgnoreCase(interest)) {
            return "Cultural".equals(category) || "Club Activity".equals(category) || "Competition".equals(category);
        }
        return category.equalsIgnoreCase(interest);
    }

    public static List<Event> getUpcomingSoon() {
        List<Event> result = new ArrayList<Event>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(7);
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            LocalDate date = event.getDate();
            if (!date.isBefore(today) && !date.isAfter(limit)) {
                result.add(event);
            }
        }
        return result;
    }

    public static List<Event> getEventsByCollege(String collegeName) {
        List<Event> result = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getCollege().equals(collegeName)) {
                result.add(events.get(i));
            }
        }
        return result;
    }

    public static int countUpcomingForCollege(String collegeName) {
        int count = 0;
        LocalDate today = LocalDate.now();
        List<Event> collegeEvents = getEventsByCollege(collegeName);
        for (int i = 0; i < collegeEvents.size(); i++) {
            Event event = collegeEvents.get(i);
            if (!event.getDate().isBefore(today) && !"Completed".equals(event.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public static List<Event> filterEvents(String query, String category, String college,
                                          String location, String status, String sortBy) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Event> result = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            boolean matchesQuery = q.isEmpty()
                    || event.getEventName().toLowerCase().contains(q)
                    || event.getCollege().toLowerCase().contains(q)
                    || event.getCategory().toLowerCase().contains(q);
            boolean matchesCategory = "All Categories".equals(category) || event.getCategory().equals(category);
            boolean matchesCollege = "All Colleges".equals(college) || event.getCollege().equals(college);
            boolean matchesLocation = "All Locations".equals(location) || event.getLocation().equals(location);
            boolean matchesStatus = "All Status".equals(status) || event.getStatus().equals(status);
            if (matchesQuery && matchesCategory && matchesCollege && matchesLocation && matchesStatus) {
                result.add(event);
            }
        }
        sortEvents(result, sortBy);
        return result;
    }

    private static void sortEvents(List<Event> list, String sortBy) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                boolean swap = false;
                if ("Date: Latest First".equals(sortBy)) {
                    swap = list.get(i).getDate().isBefore(list.get(j).getDate());
                } else if ("Name: A-Z".equals(sortBy)) {
                    swap = list.get(i).getEventName().compareToIgnoreCase(list.get(j).getEventName()) > 0;
                } else {
                    swap = list.get(i).getDate().isAfter(list.get(j).getDate());
                }
                if (swap) {
                    Event temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }

    public static boolean isRegistered(String email, int eventId) {
        for (int i = 0; i < registrations.size(); i++) {
            Registration registration = registrations.get(i);
            if (registration.getStudentEmail().equals(email) && registration.getEventId() == eventId) {
                return true;
            }
        }
        return false;
    }

    public static boolean register(User user, Event event) {
        if (isRegistered(user.getEmail(), event.getEventId())) {
            return false;
        }
        registrations.add(new Registration(user.getFullName(), user.getEmail(), event.getEventId(),
                event.getEventName(), event.getCollege(), LocalDate.now()));
        if (event.getAvailableSeats() > 0) {
            event.setAvailableSeats(event.getAvailableSeats() - 1);
        }
        return true;
    }

    public static List<Registration> getRegistrationsForEmail(String email) {
        List<Registration> result = new ArrayList<Registration>();
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getStudentEmail().equals(email)) {
                result.add(registrations.get(i));
            }
        }
        return result;
    }

    public static List<Integer> getBookmarkIds(String email) {
        if (!bookmarksByEmail.containsKey(email)) {
            bookmarksByEmail.put(email, new ArrayList<Integer>());
        }
        return bookmarksByEmail.get(email);
    }

    public static boolean isBookmarked(String email, int eventId) {
        return getBookmarkIds(email).contains(Integer.valueOf(eventId));
    }

    public static void addBookmark(String email, int eventId) {
        List<Integer> ids = getBookmarkIds(email);
        if (!ids.contains(Integer.valueOf(eventId))) {
            ids.add(Integer.valueOf(eventId));
        }
    }

    public static void removeBookmark(String email, int eventId) {
        getBookmarkIds(email).remove(Integer.valueOf(eventId));
    }

    public static List<Event> getBookmarkedEvents(String email) {
        List<Event> result = new ArrayList<Event>();
        List<Integer> ids = getBookmarkIds(email);
        for (int i = 0; i < ids.size(); i++) {
            Event event = findEventById(ids.get(i).intValue());
            if (event != null) {
                result.add(event);
            }
        }
        return result;
    }

    public static List<String> getCollegeNames() {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < colleges.size(); i++) {
            names.add(colleges.get(i).getCollegeName());
        }
        return names;
    }

    public static List<String> getLocations() {
        List<String> locations = new ArrayList<String>();
        for (int i = 0; i < events.size(); i++) {
            String location = events.get(i).getLocation();
            if (!locations.contains(location)) {
                locations.add(location);
            }
        }
        return locations;
    }

    public static void deleteEvent(Event event) {
        events.remove(event);
        for (int i = registrations.size() - 1; i >= 0; i--) {
            if (registrations.get(i).getEventId() == event.getEventId()) {
                registrations.remove(i);
            }
        }
    }
}
