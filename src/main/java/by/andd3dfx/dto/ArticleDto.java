package by.andd3dfx.dto;

import by.andd3dfx.services.validators.ExistingAuthor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ArticleDto {

    @Null(message = "Article id shouldn't be present")
    private Long id;

    @NotNull(message = "Title should be populated")
    @Size(min = 1, max = 100, message = "Title length must be between 1 and 100")
    private String title;

    @Size(max = 255, message = "Summary length shouldn't be greater than 255")
    private String summary;

    @NotNull(message = "Text should be populated")
    @Size(min = 1, message = "Text length should be 1 at least")
    private String text;

    @NotNull(message = "Author should be populated")
    @ExistingAuthor
    private AuthorDto author;

    @Null(message = "DateCreated shouldn't be populated")
    private LocalDateTime dateCreated;

    @Null(message = "DateUpdated shouldn't be populated")
    private LocalDateTime dateUpdated;
}
