package by.andd3dfx.dto;

import by.andd3dfx.services.validators.OnlyOneFieldModified;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@OnlyOneFieldModified
public class ArticleUpdateDto {

    @Size(min = 1, max = 100, message = "Title length must be between 1 and 100")
    private String title;

    @Size(max = 255, message = "Summary length shouldn't be greater than 255")
    private String summary;

    @Size(min = 1, message = "Text length should be 1 at least")
    private String text;
}