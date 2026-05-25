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
        Long eventId = activityEvent.getEventId();
        String type = activityEvent.getActivityType();

        // получаем статистику
        EventStats stats = statsMap.computeIfAbsent(eventId, k -> new EventStats());
        stats.registerActivity(); // Обновляем метку времени любой активности

        if ("VIEW".equals(type)) {
            // счетчик до 10, если 10 накопилось, цена +1
            if (stats.getViewCounter().incrementAndGet() >= 10) {
                stats.getViewCounter().addAndGet(-10); // Атомарно вычитаем 10
                triggerPriceUpdate(eventId, BigDecimal.valueOf(1), "10 просмотров");
            }
        } else if ("PURCHASE".equals(type)) {
            // счетчик до 5, если 5 накопилось, цена +5
            if (stats.getOrderCounter().incrementAndGet() >= 5) {
                stats.getOrderCounter().addAndGet(-5); // Атомарно вычитаем 5
                triggerPriceUpdate(eventId, BigDecimal.valueOf(5), "5 заказов");
            }
        }
    }

    private void triggerPriceUpdate(Long eventId, BigDecimal amount, String reason) {
        try {
            bookingClient.updateEventPrice(eventId, amount);
            System.out.println("[Analytics] Цена на мероприятие " + eventId + " ПОВЫШЕНА на " + amount + " руб. Причина: " + reason);
        } catch (Exception e) {
            System.err.println("Ошибка отправки обновления цены через Feign: " + e.getMessage());
        }
    }
}