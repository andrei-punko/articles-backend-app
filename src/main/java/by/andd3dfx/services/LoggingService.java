package by.andd3dfx.services;

import by.andd3dfx.dto.MethodCallRecord;
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

@Service
@AllArgsConstructor
public class LoggingService implements ILoggingService {

    private final static Logger logger = LoggerFactory.getLogger(LoggingService.class);

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void logMethodCall(Method method, Object[] args, Object result, boolean isSucceed) {
        MethodCallRecord methodCallRecord = buildMethodCallRecord(method, args, result, isSucceed);
        logger.info(String.format("Method call detected: %s", methodCallRecord));

        messagingTemplate.convertAndSend("/topic/logs", methodCallRecord);
    }

    private MethodCallRecord buildMethodCallRecord(Method method, Object[] args, Object result, boolean isSucceed) {
        List<String> paramNames = Arrays.stream(method.getParameters()).map(Parameter::getName).collect(Collectors.toList());
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
}
