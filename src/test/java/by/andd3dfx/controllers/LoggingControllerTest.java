package by.andd3dfx.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.services.ILoggingService;
import by.andd3dfx.util.TestUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoggingController.class)
class LoggingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ILoggingService loggingService;

    @MockBean
    private DataSource dataSource;

    @Test
    public void getLoggedRecords() throws Exception {
        final List<MethodCallRecord> items = Arrays.asList(new MethodCallRecord());

        final String timestamp = "2011-03-05";
        final LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setTimestamp(timestamp);
        given(loggingService.getLoggedRecords(criteria)).willReturn(items);

        mockMvc.perform(get("/api/v1/logs?timestamp=" + timestamp))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(TestUtil.asJsonString(items)));

        verify(loggingService).getLoggedRecords(criteria);
    }

    @Test
    public void addLoggingRecord() throws Exception {
        final MethodCallRecord loggedRecord = new MethodCallRecord();
        loggedRecord.setName("methodName");
        loggedRecord.setArgs(new HashMap<String, Object>() {{
            put("arg1", "Some value");
        }});
        loggedRecord.setTimestamp(LocalDateTime.parse("2020-05-19T16:08:32"));
        loggedRecord.setResult("Result string");
        loggedRecord.setIsSucceed(true);

        mockMvc.perform(post("/api/v1/logs")
            .content(TestUtil.asJsonString(loggedRecord))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(loggingService).addLoggingRecord(loggedRecord);
    }
}
