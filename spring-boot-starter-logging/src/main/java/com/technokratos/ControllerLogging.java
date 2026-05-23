package com.technokratos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Aspect
public class ControllerLogging {

    private  static final Logger logger = LoggerFactory.getLogger("logging");

    private final LoggingProperties properties;

    public ControllerLogging(LoggingProperties properties) {
        this.properties = properties;
    }

    @Around("execution(* com..technokratos..*Controller.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        logger.info("{} {} --- {} {} : Request/Response = {} {}ms", Instant.now(), "INFO",
                properties.getModule(), joinPoint.getSignature().getDeclaringTypeName(), result, duration);
        return result;
    }
}
