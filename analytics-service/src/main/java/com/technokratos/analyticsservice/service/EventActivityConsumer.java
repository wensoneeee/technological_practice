package com.technokratos.analyticsservice.service;

import com.technokratos.analyticsservice.client.BookingClient;
import com.technokratos.analyticsservice.config.RabbitConfig;
import com.technokratos.analyticsservice.dto.EventActivityEvent;
import com.technokratos.analyticsservice.model.EventStats;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventActivityConsumer {

    @Autowired
    private BookingClient bookingClient;

    // хранилище записей
    private final Map<Long, EventStats> statsMap = new ConcurrentHashMap<>();

    public Map<Long, EventStats> getStatsMap() {
        return statsMap;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleEventActivity(EventActivityEvent activityEvent) {
        if (activityEvent == null || activityEvent.getEventId() == null) {
            System.err.println("[Analytics] Получено пустое событие активности или eventId равен null! Пропускаем.");
            return;
        }

        Long eventId = activityEvent.getEventId();
        String type = activityEvent.getActivityType();

        EventStats stats = statsMap.computeIfAbsent(eventId, k -> new EventStats());
        stats.registerActivity();

        if ("VIEW".equals(type)) {
            if (stats.getViewCounter().incrementAndGet() >= 10) {
                stats.getViewCounter().addAndGet(-10);
                triggerPriceUpdate(eventId, BigDecimal.valueOf(1));
            }
        } else if ("PURCHASE".equals(type)) {
            if (stats.getOrderCounter().incrementAndGet() >= 5) {
                stats.getOrderCounter().addAndGet(-5);
                triggerPriceUpdate(eventId, BigDecimal.valueOf(5));
            }
        }
    }

    private void triggerPriceUpdate(Long eventId, BigDecimal amount) {
        try {
            bookingClient.updateEventPrice(eventId, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}