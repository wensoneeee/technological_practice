package com.technokratos;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerSaving extends AppenderBase<ILoggingEvent> {

    private LoggingProperties properties;

    private String filePath = "application.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void append(ILoggingEvent event) {
        if (event == null) return;

        String level = event.getLevel().toString();

        String timestamp = LocalDateTime.now().format(formatter);

        String logLine = String.format("%s [%s] %s - %s%n",
                timestamp, level, event.getLoggerName(), event.getFormattedMessage());

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            writer.write(logLine);
            writer.flush();
        } catch (IOException e) {
            addError("Ошибка записи в лог-файл", e);
        }
    }

    public void setProperties(LoggingProperties properties) {
        this.properties = properties;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
