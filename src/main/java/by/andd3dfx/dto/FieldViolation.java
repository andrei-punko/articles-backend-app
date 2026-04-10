package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Single validation issue")
public record FieldViolation(
    @Schema(description = "Request field path (empty for class-level constraints)")
    String field,
    @Schema(description = "Validation message")
    String message
) {
}
