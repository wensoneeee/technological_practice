package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class ControllerLogging {

    private  static final Logger log = LoggerFactory.getLogger("logging");

    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());

        log.info("[CONTROLLER] {}.{}() - Входящий запрос: {}", className, methodName, args);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("[CONTROLLER] {}.{}() - Успех ({} мс). Ответ: {}", className, methodName, executionTime, result);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[CONTROLLER] {}.{}() - ОШИБКА ({} мс). Причина: {}", className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
