/**
 *
 */
package chapter6.beans;

/**
 * @author trainee1207
 *
 */
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int id;
    private int userId;
    private String text;
    private Date createdDate;
    private Date updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updateDate) {
        this.updatedDate = updateDate;
    }
}
