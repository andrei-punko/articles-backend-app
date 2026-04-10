package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Criteria for searching logged method calls")
public class LoggingSearchCriteria {

    @JsonProperty("id")
    @Schema(description = "Record id filter")
    private Long idCap;

    @Schema(description = "Timestamp filter")
    private String timestamp;

    @Schema(description = "Page size", defaultValue = "50")
    private Integer pageSize;
}
