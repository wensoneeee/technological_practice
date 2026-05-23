package com.technokratos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "logging")
public class LoggingProperties {

    private String level = "DEBUG";
    private String output = "file";
    private String module = "project";
    private String filePath = "application.log";
}
