package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;

@Aspect
public class RepositoryLogging {
    private static final Logger log = LoggerFactory.getLogger("logging");

    private final LoggingProperties loggingProperties;

    public RepositoryLogging(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Around("within(@org.springframework.stereotype.Repository *)")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("[REPOSITORY] Выполнение SQL-запроса через метод: {}.{}()", className, methodName);

        long start = System.currentTimeMillis();
        try {

            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.debug("[REPOSITORY] База данных успешно вернула результат для: {}.{}() ({} мс)",
                    className, methodName, executionTime);
            return result;
        } catch (Throwable e) {
            log.error("[REPOSITORY] Ошибка базы данных в {}.{}(). Причина: {}", className, methodName, e.getMessage());
            throw e;
        }
    }

}
