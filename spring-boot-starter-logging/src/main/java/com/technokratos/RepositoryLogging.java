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

    private final LoggingProperties properties;

    public RepositoryLogging(LoggingProperties properties) {
        this.properties = properties;
    }


    @Around("within(@org.springframework.stereotype.Repository *)")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.debug("[REPO] {}.{}() - Успех ({} мс)", className, methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[REPO] {}.{}() - ОШИБКА БД ({} мс). Причина: {}", className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }

}
