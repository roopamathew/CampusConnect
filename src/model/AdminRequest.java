package model;

import java.time.LocalDate;

/**
 * Request submitted by a college representative seeking administrator access.
 * Reviewed and approved/rejected by the Super Admin.
 */
public class AdminRequest {

    private int requestId;
    private String fullName;
    private String email;
    private String phone;
    private String college;
    private String designation;
    private String reason;
    private LocalDate requestDate;
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private LocalDate reviewedDate;
    private String reviewerNotes;

    public AdminRequest(int requestId, String fullName, String email, String phone,
                        String college, String designation, String reason,
                        LocalDate requestDate, String status) {
        this.requestId = requestId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.college = college;
        this.designation = designation;
        this.reason = reason;
        this.requestDate = requestDate;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(LocalDate reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public String getReviewerNotes() {
        return reviewerNotes;
    }

    public void setReviewerNotes(String reviewerNotes) {
        this.reviewerNotes = reviewerNotes;
    }
}
