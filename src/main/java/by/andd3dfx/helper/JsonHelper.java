package by.andd3dfx.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JsonHelper {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String objectToString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public Map<String, Object> jsonStringToMap(String jsonString) {
        return objectMapper.readValue(jsonString, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public Object jsonStringToDefiniteType(String jsonString, String resultType) {
        if (resultType == null) {
            return jsonString;
        }
        return objectMapper.readValue(jsonString, Class.forName(resultType));
    }
}
