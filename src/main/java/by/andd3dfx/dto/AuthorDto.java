package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AuthorDto {

    @ApiModelProperty(notes = "The database generated author ID")
    private Long id;

    @ApiModelProperty(notes = "Author's first name")
    private String firstName;

    @ApiModelProperty(notes = "Author's last name")
    private String lastName;
}
