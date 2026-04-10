package by.andd3dfx.dto;

import by.andd3dfx.validators.OnlyOneFieldModified;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@OnlyOneFieldModified
@Schema(description = "Single-field article update (exactly one property must be set)")
public class ArticleUpdateDto {

    @Size(min = 1, max = 100, message = "Title length must be between 1 and 100")
    @Schema(description = "Article's title")
    private String title;

    @Size(max = 255, message = "Summary length shouldn't be greater than 255")
    @Schema(description = "Article's summary")
    private String summary;

    @Size(min = 1, message = "Text length should be 1 at least")
    @Schema(description = "Article's text")
    private String text;
}
