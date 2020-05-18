package by.andd3dfx.services;

import java.lang.reflect.Method;

public interface ILoggingService {

    void logMethodCall(Method methodName, Object[] args, Object result, boolean isSucceed);
}
