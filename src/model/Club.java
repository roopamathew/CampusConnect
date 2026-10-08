package model;

public class Club {

    private int clubId;
    private String clubName;
    private String college;
    private String category;
    private String description;
    private String contactPerson;
    private String contactEmail;

    public Club(int clubId, String clubName, String college, String category,
                String description, String contactPerson, String contactEmail) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.college = college;
        this.category = category;
        this.description = description;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
