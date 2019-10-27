package by.andd3dfx.dto;

import by.andd3dfx.services.validators.ExistingAuthor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ArticleDto {

    public interface New {

    }

    @Null(groups = {New.class}, message = "Article id shouldn't be present")
    private Long id;

    @NotNull(groups = {New.class}, message = "Title should be populated")
    @Size(min = 1, max = 100, groups = {New.class}, message = "Title length must be between 1 and 100")
    private String title;

    @Size(max = 255, groups = {New.class}, message = "Summary length shouldn't be greater than 255")
    private String summary;

    @NotNull(groups = {New.class}, message = "Text should be populated")
    @Size(min = 1, groups = {New.class}, message = "Text length should be 1 at least")
    private String text;

    @NotNull(groups = {New.class}, message = "Author should be populated")
    @ExistingAuthor(groups = {New.class})
    private AuthorDto author;

    @Null(groups = {New.class}, message = "DateCreated shouldn't be populated")
    private LocalDateTime dateCreated;

    @Null(groups = {New.class}, message = "DateUpdated shouldn't be populated")
    private LocalDateTime dateUpdated;
}
