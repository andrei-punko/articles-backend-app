package by.andd3dfx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggingSearchCriteria {

    @JsonProperty("id")
    private Long idCap;
    private String timestamp;

    @ApiParam(value = "Page size", defaultValue = "50")
    private Integer pageSize;
}
