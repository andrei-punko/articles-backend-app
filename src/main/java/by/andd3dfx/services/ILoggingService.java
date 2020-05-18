package by.andd3dfx.services;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import java.lang.reflect.Method;
import java.util.List;

public interface ILoggingService {

    void logMethodCall(Method methodName, Object[] args, Object result, boolean isSucceed);

    List<MethodCallRecord> getLoggedRecords(LoggingSearchCriteria criteria);

    void addLoggingRecord(MethodCallRecord loggedRecord);
}
