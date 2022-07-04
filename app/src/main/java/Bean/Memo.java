package Bean;

import java.util.Date;

public class Memo {
    private String userEmail;
    private Date createDate;
    private Date lastModified;
    private String title;
    private String content;
    private String id;

    public Memo() {
    }

    public Memo(String userEmail, Date createDate, Date lastModified, String title, String content, String id) {
        this.userEmail = userEmail;
        this.createDate = createDate;
        this.lastModified = lastModified;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
