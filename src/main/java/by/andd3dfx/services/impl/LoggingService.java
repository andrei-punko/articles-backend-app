package by.andd3dfx.services.impl;

import by.andd3dfx.annotations.CustomLog;
import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.helper.JsonHelper;
import by.andd3dfx.persistence.dao.LoggingRepository;
import by.andd3dfx.persistence.entities.LoggedRecord;
import by.andd3dfx.services.ILoggingService;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LoggingService implements ILoggingService {

    private final static Logger logger = LoggerFactory.getLogger(LoggingService.class);

    private final LoggingRepository loggingRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JsonHelper jsonHelper;

    @Override
    @Transactional
    public void logMethodCall(Method method, Object[] args, Object result, boolean isSucceed) {
        MethodCallRecord methodCallRecord = buildMethodCallRecord(method, args, result, isSucceed);
        logger.info(String.format("Method call detected: %s", methodCallRecord));

        LoggedRecord recordToSave = buildLoggedRecordEntity(methodCallRecord);
        loggingRepository.save(recordToSave);

        messagingTemplate.convertAndSend("/topic/logs", methodCallRecord);
    }

    private MethodCallRecord buildMethodCallRecord(Method method, Object[] args, Object result, boolean isSucceed) {
        List<String> paramNames = Arrays.stream(method.getParameters())
                .map(Parameter::getName)
                .toList();
        if (paramNames.size() != args.length) {
            throw new IllegalArgumentException("Parameter names amount differ from args amount!");
        }
        Map<String, Object> argsMap = new HashMap<>();
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            Object paramValue = args[i];
            argsMap.put(paramName, paramValue);
        }

        MethodCallRecord record = new MethodCallRecord();
        record.setName(method.getName());
        record.setArgs(argsMap);
        record.setResult(result);
        record.setTimestamp(LocalDateTime.now());
        record.setIsSucceed(isSucceed);
        return record;
    }

    private LoggedRecord buildLoggedRecordEntity(MethodCallRecord methodCallRecord) {
        LoggedRecord loggingRecord = new LoggedRecord();
        loggingRecord.setMethodName(methodCallRecord.getName());
        loggingRecord.setArguments(jsonHelper.objectToString(methodCallRecord.getArgs()));
        Object result = methodCallRecord.getResult();
        loggingRecord.setResult(jsonHelper.objectToString(result));
        if (result != null) {
            loggingRecord.setResultType(result.getClass().getName());
        }
        loggingRecord.setTimestamp(methodCallRecord.getTimestamp());
        loggingRecord.setIsSucceed(methodCallRecord.getIsSucceed());
        return loggingRecord;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MethodCallRecord> getLoggedRecords(LoggingSearchCriteria criteria) {
        logger.info(String.format("Read logged records: criteria=%s", criteria));

        List<LoggedRecord> entities = loggingRepository.findByCriteria(criteria);
        return entities.stream().map(entity ->
            new MethodCallRecord() {{
                setName(entity.getMethodName());
                setArgs(jsonHelper.jsonStringToMap(entity.getArguments()));
                setResult(jsonHelper.jsonStringToDefiniteType(entity.getResult(), entity.getResultType()));
                setTimestamp(entity.getTimestamp());
                setIsSucceed(entity.getIsSucceed());
            }}).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CustomLog
    public void addLoggingRecord(MethodCallRecord methodCallRecord) {
        LoggedRecord recordToSave = buildLoggedRecordEntity(methodCallRecord);
        loggingRepository.save(recordToSave);
    }
}
