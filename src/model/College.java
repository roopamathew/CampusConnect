package model;

public class College {

    private int collegeId;
    private String collegeName;
    private String location;
    private String email;
    private String description;

    public College(int collegeId, String collegeName, String location, String email, String description) {
        this.collegeId = collegeId;
        this.collegeName = collegeName;
        this.location = location;
        this.email = email;
        this.description = description;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return collegeName;
    }
}
