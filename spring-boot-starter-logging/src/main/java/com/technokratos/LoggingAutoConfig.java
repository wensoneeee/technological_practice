package com.technokratos;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingAutoConfig {

    @Bean
    public LoggerSaving loggerSaving(LoggingProperties properties) {
        LoggerSaving loggerSaving = new LoggerSaving();
        loggerSaving.setProperties(properties);
        loggerSaving.setFilePath(properties.getFilePath());
        loggerSaving.start();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("logging");
        Level level = Level.toLevel(properties.getLevel(), Level.INFO);
        logger.setLevel(level);
        logger.addAppender(loggerSaving);
        logger.setAdditive(false);
        return loggerSaving;
    }

    @Bean
    public RepositoryLogging RepositoryLoggingAspect(LoggingProperties properties) {
        return new RepositoryLogging(properties);
    }

    @Bean
    public ServiceLogging serviceLoggingAspect(LoggingProperties properties) {
        return new ServiceLogging(properties);
    }

    @Bean
    public ControllerLogging controllerLoggingAspect(LoggingProperties properties) {
        return new ControllerLogging(properties);
    }

    @Bean
    public ComponentLogging componentLoggingAspect(LoggingProperties properties) {
        return new ComponentLogging(properties);
    }
}