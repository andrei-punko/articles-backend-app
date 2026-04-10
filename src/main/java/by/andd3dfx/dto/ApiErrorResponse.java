package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Unified API error payload")
public record ApiErrorResponse(
    @Schema(description = "Error time (UTC)")
    Instant timestamp,
    @Schema(description = "HTTP status code")
    int status,
    @Schema(description = "Short error category")
    String error,
    @Schema(description = "Human-readable message")
    String message,
    @Schema(description = "Request path")
    String path,
    @Schema(description = "Field-level validation failures")
    List<FieldViolation> violations
) {

    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, null);
    }

    public static ApiErrorResponse validation(String path, List<FieldViolation> violations) {
        return new ApiErrorResponse(
            Instant.now(),
            400,
            "Validation Failed",
            "Request validation failed",
            path,
            violations
        );
    }
}
