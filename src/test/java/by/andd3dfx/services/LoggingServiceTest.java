package by.andd3dfx.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.helper.JsonHelper;
import by.andd3dfx.persistence.dao.LoggingRepository;
import by.andd3dfx.persistence.entities.LoggedRecord;
import by.andd3dfx.services.impl.LoggingService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
class LoggingServiceTest {

    @Mock
    LoggingRepository loggingRepository;
    @Mock
    JsonHelper jsonHelper;
    @Captor
    ArgumentCaptor<LoggedRecord> captor;

    @InjectMocks
    LoggingService loggingService;

    @Test
    void getLoggedRecords() {
        final LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        LoggedRecord loggedRecord = new LoggedRecord();
        loggedRecord.setMethodName("method-name");
        loggedRecord.setTimestamp(LocalDateTime.now());
        loggedRecord.setIsSucceed(true);
        loggedRecord.setResult("some-result");
        final List<LoggedRecord> entities = Arrays.asList(loggedRecord);
        when(loggingRepository.findByCriteria(criteria)).thenReturn(entities);
        final Map<String, Object> ARGS = new HashMap<>();
        when(jsonHelper.jsonStringToMap(loggedRecord.getArguments())).thenReturn(ARGS);
        final Object RESULT = new Object();
        when(jsonHelper.jsonStringToDefiniteType(loggedRecord.getResult(), loggedRecord.getResultType())).thenReturn(RESULT);

        List<MethodCallRecord> result = loggingService.getLoggedRecords(criteria);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getName(), is(loggedRecord.getMethodName()));
        assertThat(result.get(0).getArgs(), is(ARGS));
        assertThat(result.get(0).getResult(), is(RESULT));
        assertThat(result.get(0).getTimestamp(), is(loggedRecord.getTimestamp()));
        assertThat(result.get(0).getIsSucceed(), is(loggedRecord.getIsSucceed()));
    }

    @Test
    void addLoggingRecord() {
        MethodCallRecord methodCallRecord = buildMethodCallRecord();
        when(jsonHelper.objectToString(methodCallRecord.getArgs())).thenReturn("STRING1");
        when(jsonHelper.objectToString(methodCallRecord.getResult())).thenReturn("STRING2");

        loggingService.addLoggingRecord(methodCallRecord);

        verify(loggingRepository).save(captor.capture());
        LoggedRecord loggedRecord = captor.getValue();
        assertThat(loggedRecord.getMethodName(), is("some-name"));
        assertThat(loggedRecord.getArguments(), is("STRING1"));
        assertThat(loggedRecord.getResult(), is("STRING2"));
        assertThat(loggedRecord.getResultType(), is(String.class.getName()));
        assertThat(loggedRecord.getTimestamp(), is(methodCallRecord.getTimestamp()));
        assertThat(loggedRecord.getIsSucceed(), is(true));
    }

    private MethodCallRecord buildMethodCallRecord() {
        MethodCallRecord methodCallRecord = new MethodCallRecord();
        methodCallRecord.setName("some-name");
        methodCallRecord.setArgs(new HashMap<>());
        methodCallRecord.setResult("Some alien string");
        methodCallRecord.setTimestamp(LocalDateTime.now());
        methodCallRecord.setIsSucceed(true);
        return methodCallRecord;
    }
}
