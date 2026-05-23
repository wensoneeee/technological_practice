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
    private static final Logger logger = LoggerFactory.getLogger("logging");

    private final LoggingProperties properties;

    public RepositoryLogging(LoggingProperties properties) {
        this.properties = properties;
    }


    @Around("execution(* com..technokratos..*Repository.*(..))")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        logger.debug("{} {} --- {} {} : Parameters = {} and result = {} {}ms", Instant.now(), "DEBUG",
                properties.getModule(), joinPoint.getSignature().getDeclaringTypeName(),
                Arrays.toString(joinPoint.getArgs()), result, duration);
        return result;
    }

}
