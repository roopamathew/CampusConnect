package model;

/**
 * Simple user model used for login and profile display.
 */
public class User {

    private String username;
    private String password;
    private String role;
    private String fullName;
    private String email;
    private String college;
    private String interest;
    private String course = "";
    private String branch = "";
    private String yearSemester = "";
    private String graduationYear = "";
    private String designation = "";
    private String phone = "";
    private String accountStatus = "Active";
    private String imagePath = "";

    public User(String username, String password, String role, String fullName,
                String email, String college, String interest) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.college = college;
        this.interest = interest;
    }

    public User(String username, String password, String role, String fullName,
                String email, String college, String interest, String course,
                String branch, String yearSemester, String graduationYear,
                String designation, String phone) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.college = college;
        this.interest = interest;
        this.course = course == null ? "" : course;
        this.branch = branch == null ? "" : branch;
        this.yearSemester = yearSemester == null ? "" : yearSemester;
        this.graduationYear = graduationYear == null ? "" : graduationYear;
        this.designation = designation == null ? "" : designation;
        this.phone = phone == null ? "" : phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) {
        this.yearSemester = yearSemester;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getImagePath() {
        return imagePath != null ? imagePath : "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
    }

    public boolean hasImage() {
        return imagePath != null && !imagePath.trim().isEmpty();
    }
}
