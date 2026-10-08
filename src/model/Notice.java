package model;

import java.time.LocalDate;

public class Notice {

    private int noticeId;
    private String title;
    private String description;
    private String college;
    private LocalDate postedDate;
    private String type;

    public Notice(int noticeId, String title, String description, String college,
                  LocalDate postedDate, String type) {
        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.college = college;
        this.postedDate = postedDate;
        this.type = type;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
