package by.andd3dfx.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class ArticleDto {

    public interface New {

    }

    public interface Update {

    }

    @Null(groups = {New.class})
    @NotNull(groups = {Update.class})
    private Long id;

    @NotNull(groups = {New.class, Update.class})
    @Size(min = 1, max = 100, groups = {New.class, Update.class})
    private String title;

    @Size(max = 255, groups = {New.class, Update.class})
    private String summary;

    @NotNull(groups = {New.class, Update.class})
    @Size(min = 1, groups = {New.class, Update.class})
    private String text;

    @NotNull(groups = {New.class})
    @Null(groups = {Update.class})
    private AuthorDto author;

    @Null(groups = {New.class, Update.class})
    private Date dateCreated;

    @Null(groups = {New.class, Update.class})
    private Date dateUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
