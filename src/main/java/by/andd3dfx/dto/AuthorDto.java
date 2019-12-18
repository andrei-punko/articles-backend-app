package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AuthorDto {

    private Long id;
    private String firstName;
    private String lastName;
}
