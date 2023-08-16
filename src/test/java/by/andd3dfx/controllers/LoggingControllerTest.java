package by.andd3dfx.controllers;

import by.andd3dfx.ArticlesBackendAppApplication;
import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.services.ILoggingService;
import by.andd3dfx.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
@WebAppConfiguration
class LoggingControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ILoggingService loggingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void getLoggedRecords() throws Exception {
        final List<MethodCallRecord> items = Arrays.asList(new MethodCallRecord());

        final String timestamp = "2020-05-19T16:08:32";
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
        loggedRecord.setArgs(new HashMap<>() {{
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
