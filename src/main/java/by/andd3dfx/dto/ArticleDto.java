package by.andd3dfx.dto;

import by.andd3dfx.dto.ArticleDto.Update;
import by.andd3dfx.services.validators.ExistingAuthor;
import by.andd3dfx.services.validators.OnlyOneFieldModified;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@OnlyOneFieldModified(fields = {"title", "summary", "text"}, groups = {Update.class})
public class ArticleDto {

    public interface New {

    }

    public interface Update {

    }

    @Null(groups = {New.class, Update.class}, message = "Article id shouldn't be present")
    private Long id;

    @NotNull(groups = {New.class}, message = "Title should be populated")
    @Size(min = 1, max = 100, groups = {New.class, Update.class}, message = "Title length must be between 1 and 100")
    private String title;

    @Size(max = 255, groups = {New.class, Update.class}, message = "Summary length shouldn't be greater than 255")
    private String summary;

    @NotNull(groups = {New.class}, message = "Text should be populated")
    @Size(min = 1, groups = {New.class, Update.class}, message = "Text length should be 1 at least")
    private String text;

    @NotNull(groups = {New.class}, message = "Author should be populated")
    @ExistingAuthor(groups = {New.class})
    @Null(groups = {Update.class}, message = "Author shouldn't be present")
    private AuthorDto author;

    @Null(groups = {New.class, Update.class}, message = "DateCreated shouldn't be populated")
    private Date dateCreated;

    @Null(groups = {New.class, Update.class}, message = "DateUpdated shouldn't be populated")
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
