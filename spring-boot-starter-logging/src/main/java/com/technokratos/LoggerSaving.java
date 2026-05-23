package com.technokratos;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoggerSaving extends AppenderBase<ILoggingEvent> {

    private LoggingProperties properties;
    private String filePath = "application.log";
    private PrintWriter fileWriter;

    public void setProperties(LoggingProperties properties) {
        this.properties = properties;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void start() {
        if ("file".equals(properties.getOutput())) {
            try {
                this.fileWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8)));
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }
        }
        super.start();
    }

    @Override
    protected synchronized void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }

        String message = event.getFormattedMessage();

        if ("console".equals(properties.getOutput())) {
            System.out.println(message);
        } else {
            fileWriter.println(message);
            fileWriter.flush();
        }
    }

    @Override
    public void stop() {
        synchronized (this) {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
        super.stop();
    }
}
