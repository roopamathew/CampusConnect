package model;

import java.time.LocalDate;

public class Registration {

    private String studentName;
    private String studentEmail;
    private int eventId;
    private String eventName;
    private String college;
    private LocalDate registrationDate;

    public Registration(String studentName, String studentEmail, int eventId,
                        String eventName, String college, LocalDate registrationDate) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.eventId = eventId;
        this.eventName = eventName;
        this.college = college;
        this.registrationDate = registrationDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
