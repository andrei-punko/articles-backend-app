package by.andd3dfx.annotations;

import by.andd3dfx.services.ILoggingService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomLogAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final static Logger logger = LoggerFactory.getLogger(CustomLogAnnotationBeanPostProcessor.class);
    private final ILoggingService loggingService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> type = bean.getClass();

        Set<String> methodsSet = Arrays.stream(type.getDeclaredMethods())
            .filter(declaredMethod -> declaredMethod.isAnnotationPresent(CustomLog.class))
            .map(Method::getName).collect(Collectors.toSet());

        if (!methodsSet.isEmpty()) {
            return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), (proxy, method, args) -> {
                try {
                    Object returnedValue = method.invoke(bean, args);
                    logCall(methodsSet, method, args, returnedValue, true);
                    return returnedValue;
                } catch (Exception ex) {
                    logCall(methodsSet, method, args, extractCause(ex), false);
                    throw ex;
                }
            });
        }

        return bean;
    }

    private void logCall(Set<String> methodsSet, Method method, Object[] args, Object returnedValue, boolean isSucceed) {
        if (methodsSet.contains(method.getName())) {
            loggingService.logMethodCall(method, args, returnedValue, isSucceed);
        }
    }

    private String extractCause(Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return StringUtils.left(cause.getClass().getName(), 250);
    }
}
