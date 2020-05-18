package by.andd3dfx.dto;

import by.andd3dfx.dto.serializers.LDTFromStringJsonDeserializer;
import by.andd3dfx.dto.serializers.LocalDateTimeToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class MethodCallRecord {

    private String name;
    private Map<String, Object> args;
    private Object result;

    @JsonSerialize(using = LocalDateTimeToStringSerializer.class)
    @JsonDeserialize(using = LDTFromStringJsonDeserializer.class)
    private LocalDateTime timestamp;
    private Boolean isSucceed;
}
