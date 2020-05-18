package by.andd3dfx.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static String asJsonString(Object obj) {
        try {
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
