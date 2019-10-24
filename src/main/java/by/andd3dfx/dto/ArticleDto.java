package by.andd3dfx.dto;

import by.andd3dfx.dto.ArticleDto.Update;
import by.andd3dfx.services.validators.ExistingAuthor;
import by.andd3dfx.services.validators.OnlyOneFieldModified;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
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
}
