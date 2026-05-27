package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;

@Aspect
public class ServiceLogging {
    private  static final Logger log = LoggerFactory.getLogger("logging");

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[SERVICE] {}.{}() - Запуск", className, methodName);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("[SERVICE] {}.{}() - Выполнено ({} мс)", className, methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[SERVICE] {}.{}() - ОШИБКА ({} мс). Причина: {}", className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
