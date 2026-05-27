package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ControllerLogging {

    private  static final Logger log = LoggerFactory.getLogger("logging");

    private final LoggingProperties loggingProperties;

    public ControllerLogging(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }
    @Around("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[CONTROLLER] Входящий запрос в {}.{}()", className, methodName);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("[CONTROLLER] {}.{}() успешно вернул ответ ({} мс)", className, methodName, executionTime);
            return result;
        } catch (Throwable e) {
            log.error("[CONTROLLER] Сбой в контроллере {}.{}(). Ошибка: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}
