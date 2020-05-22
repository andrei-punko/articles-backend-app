package by.andd3dfx.persistence.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.persistence.entities.LoggedRecord;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class LoggingRepositoryCustomImplTest {

    @Autowired
    private LoggingRepository repository;

    private LoggedRecord entity;
    private LoggedRecord entity2;
    private LoggedRecord entity3;

    @BeforeEach
    public void setup() {
        entity = buildLoggedRecord("M1", "Args1", "res1", LocalDateTime.parse("2010-12-03T10:15:30"));
        entity2 = buildLoggedRecord("M2", "Args2", 34, LocalDateTime.parse("2011-12-03T10:15:30"));
        entity3 = buildLoggedRecord("M3", "Args3", "res3", LocalDateTime.parse("2012-12-03T10:15:30"));
        repository.saveAll(Arrays.asList(entity, entity2, entity3));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void findByCriteria() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(3));
        assertThat("Wrong records[0]", result.get(0), is(entity3));
        assertThat("Wrong records[1]", result.get(1), is(entity2));
        assertThat("Wrong records[2]", result.get(2), is(entity));
    }

    @Test
    public void findByCriteriaWithId() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setIdCap(entity3.getId());

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Wrong records[0]", result.get(0), is(entity2));
        assertThat("Wrong records[1]", result.get(1), is(entity));
    }

    @Test
    public void findByCriteriaWithPageSize() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setPageSize(2);

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Wrong records[0]", result.get(0), is(entity3));
        assertThat("Wrong records[1]", result.get(1), is(entity2));
    }

    @Test
    public void findByCriteriaWithIdNPageSize() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setIdCap(entity2.getId());
        criteria.setPageSize(2);

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(1));
        assertThat("Wrong records[0]", result.get(0), is(entity));
    }

    @Test
    public void findByCriteriaWithTimestamp() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setTimestamp("2011-11-03T06:57:20.852Z");

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Wrong records[0]", result.get(0), is(entity3));
        assertThat("Wrong records[1]", result.get(1), is(entity2));
    }

    @Test
    public void findByCriteriaWithTimestampNId() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setTimestamp("2011-11-03T06:57:20.852Z");
        criteria.setIdCap(entity3.getId());

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(1));
        assertThat("Wrong records[0]", result.get(0), is(entity2));
    }

    @Test
    public void findByCriteriaWithTimestampNPageSize() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setTimestamp("2011-11-03T06:57:20.852Z");
        criteria.setPageSize(1);

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(1));
        assertThat("Wrong records[0]", result.get(0), is(entity3));
    }

    @Test
    public void findByCriteriaWithTimestampNPageSizeNId() {
        LoggingSearchCriteria criteria = new LoggingSearchCriteria();
        criteria.setTimestamp("2011-11-03T06:57:20.852Z");
        criteria.setPageSize(2);
        criteria.setIdCap(entity3.getId());

        List<LoggedRecord> result = repository.findByCriteria(criteria);

        assertThat("Wrong records amount", result.size(), is(1));
        assertThat("Wrong records[0]", result.get(0), is(entity2));
    }

    private LoggedRecord buildLoggedRecord(String methodName, String args, Object result, LocalDateTime parse) {
        LoggedRecord record = new LoggedRecord();
        record.setMethodName(methodName);
        record.setArguments(args);
        record.setResult(String.valueOf(result));
        record.setResultType(result.getClass().getName());
        record.setTimestamp(parse);
        record.setIsSucceed(true);
        return record;
    }
}