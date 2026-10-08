package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event organised by a college. All screens read this object from DummyData.
 */
public class Event {

    private int eventId;
    private String eventName;
    private String description;
    private String category;
    private String college;
    private String venue;
    private String location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate registrationDeadline;
    private String contactPerson;
    private String contactEmail;
    private int maxParticipants;
    private int availableSeats;
    private String status;

    public Event(int eventId, String eventName, String description, String category,
                 String college, String venue, String location, LocalDate date,
                 LocalTime startTime, LocalTime endTime, LocalDate registrationDeadline,
                 String contactPerson, String contactEmail, int maxParticipants,
                 int availableSeats, String status) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.category = category;
        this.college = college;
        this.venue = venue;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registrationDeadline = registrationDeadline;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.maxParticipants = maxParticipants;
        this.availableSeats = availableSeats;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(LocalDate registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return eventName;
    }
}
