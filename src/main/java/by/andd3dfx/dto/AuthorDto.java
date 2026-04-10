package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@Schema(description = "Author")
public class AuthorDto {

    @Schema(description = "The database generated author ID")
    private Long id;

    @Schema(description = "Author's first name")
    private String firstName;

    @Schema(description = "Author's last name")
    private String lastName;
}
