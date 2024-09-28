package by.andd3dfx.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonHelperTest {

    private JsonHelper jsonHelper;

    @BeforeEach
    void setup() {
        jsonHelper = new JsonHelper(TestUtil.getMapper());
    }

    @Test
    public void objectToString() {
        final MethodCallRecord record = new MethodCallRecord();
        record.setName("Name");
        record.setArgs(new HashMap<String, Object>() {{
            put("param1", 35L);
            put("param2", "Some string");
        }});
        record.setResult("Result");
        record.setTimestamp(LocalDateTime.parse("2007-12-03T10:15:30"));
        record.setIsSucceed(true);

        String resultJsonString = jsonHelper.objectToString(record);

        assertThat(resultJsonString,
            is("{\"name\":\"Name\",\"args\":{\"param1\":35,\"param2\":\"Some string\"},\"result\":\"Result\",\"timestamp\":\"2007-12-03T10:15:30Z\",\"isSucceed\":true}"));
    }

    @Test
    public void jsonStringToMap() {
        Map<String, Object> resultMap = jsonHelper.jsonStringToMap("{\"param1\":35,\"param2\":\"Some string\"}");

        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("param1", 35);
            put("param2", "Some string");
        }};
        checkAreMapsEqual(resultMap, expectedMap);
    }

    @Test
    public void jsonStringToDefiniteType() {
        String jsonString = "{\"name\":\"Name\",\"args\":{\"param1\":35,\"param2\":\"Some string\"},\"result\":\"Result\",\"timestamp\":\"2007-12-03T10:15:30Z\"}";
        String className = MethodCallRecord.class.getName();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("param1", 35);
            put("param2", "Some string");
        }};

        Object resultObject = jsonHelper.jsonStringToDefiniteType(jsonString, className);

        MethodCallRecord methodCallRecord = (MethodCallRecord) resultObject;
        assertThat(methodCallRecord.getName(), is("Name"));
        checkAreMapsEqual(methodCallRecord.getArgs(), expectedMap);
        assertThat(methodCallRecord.getResult(), is("Result"));
        assertThat(methodCallRecord.getTimestamp(), is(LocalDateTime.parse("2007-12-03T10:15:30")));
    }

    private void checkAreMapsEqual(Map<String, Object> map1, Map<String, Object> map2) {
        assertThat(map1.keySet().equals(map2.keySet()), is(true));
        map1.keySet().stream().forEach(key -> {
            assertThat(map1.get(key), is(map2.get(key)));
        });
    }
}
