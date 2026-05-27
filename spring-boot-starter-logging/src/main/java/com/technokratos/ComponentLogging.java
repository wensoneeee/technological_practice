package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Aspect
public class ComponentLogging {

    private static final Logger log = LoggerFactory.getLogger("logging");

    @Around("within(@org.springframework.stereotype.Component *) " +
            "&& !within(@org.springframework.stereotype.Service *) " +
            "&& !within(@org.springframework.stereotype.Repository *) " +
            "&& !within(@org.springframework.stereotype.Controller *) " +
            "&& !within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logComponent(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[COMPONENT] {}.{}() - Запуск универсального компонента", className, methodName);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("[COMPONENT] {}.{}() - Выполнено успешно ({} мс)", className, methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[COMPONENT] {}.{}() - ОШИБКА ({} мс). Причина: {}", className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
