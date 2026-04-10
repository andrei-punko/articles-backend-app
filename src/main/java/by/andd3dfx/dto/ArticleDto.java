package by.andd3dfx.dto;

import by.andd3dfx.validators.ExistingAuthor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@Schema(description = "Article payload")
public class ArticleDto {

    @Null(message = "Article id shouldn't be present")
    @Schema(description = "The database generated article ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Title should be populated")
    @Size(min = 1, max = 100, message = "Title length must be between 1 and 100")
    @Schema(description = "Article's title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 255, message = "Summary length shouldn't be greater than 255")
    @Schema(description = "Article's summary")
    private String summary;

    @NotNull(message = "Text should be populated")
    @Size(min = 1, message = "Text length should be 1 at least")
    @Schema(description = "Article's text", requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;

    @NotNull(message = "Author should be populated")
    @ExistingAuthor
    @Schema(description = "Article's author", requiredMode = Schema.RequiredMode.REQUIRED)
    private AuthorDto author;

    @Null(message = "DateCreated shouldn't be populated")
    @Schema(description = "Date & time of article creation", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateCreated;

    @Null(message = "DateUpdated shouldn't be populated")
    @Schema(description = "Date & time of article update", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateUpdated;
}
