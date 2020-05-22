package by.andd3dfx.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.andd3dfx.configs.SecurityConfig;
import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.services.ILoggingService;
import by.andd3dfx.util.TestUtil;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

        mockMvc.perform(get("/api/v2/logs?timestamp=" + timestamp))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(TestUtil.asJsonString(items)));

        verify(loggingService).getLoggedRecords(criteria);
    }

    @Test
    public void addLoggingRecord() throws Exception {
        MethodCallRecord loggedRecords = new MethodCallRecord();

        mockMvc.perform(post("/api/v2/logs"))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(loggingService).addLoggingRecord(loggedRecords);
    }
}
