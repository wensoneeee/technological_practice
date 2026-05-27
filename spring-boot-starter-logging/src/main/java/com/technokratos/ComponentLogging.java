package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Aspect
public class ComponentLogging {

    private static final Logger log = LoggerFactory.getLogger("logging");

    private final LoggingProperties loggingProperties;

    public ComponentLogging(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Around("within(com.technokratos..*) " +
            "&& within(@org.springframework.stereotype.Component *) " +
            "&& !within(jakarta.servlet.Filter+) " +
            "&& !within(@org.springframework.context.annotation.Configuration *) " +
            "&& !within(@org.springframework.stereotype.Service *) " +
            "&& !within(@org.springframework.stereotype.Repository *) " +
            "&& !within(@org.springframework.stereotype.Controller *) " +
            "&& !within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logComponent(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("[COMPONENT] {}.{}() - Старт компонента", className, methodName);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.debug("[COMPONENT] {}.{}() - Завершено тех. действие ({} мс)", className, methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;

            log.error("[COMPONENT] {}.{}() - Ошибка компонента ({} мс). Причина: {}",
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
