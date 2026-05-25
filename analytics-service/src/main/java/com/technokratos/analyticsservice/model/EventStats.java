package com.technokratos.analyticsservice.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class EventStats {
    private final AtomicInteger viewCounter = new AtomicInteger(0);
    private final AtomicInteger orderCounter = new AtomicInteger(0);
    private volatile LocalDateTime lastActivityTime = LocalDateTime.now();

    public void registerActivity() {
        this.lastActivityTime = LocalDateTime.now();
    }
}