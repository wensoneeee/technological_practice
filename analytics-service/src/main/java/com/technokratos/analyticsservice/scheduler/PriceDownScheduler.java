package com.technokratos.analyticsservice.scheduler;

import com.technokratos.analyticsservice.client.BookingClient;
import com.technokratos.analyticsservice.model.EventStats;
import com.technokratos.analyticsservice.service.EventActivityConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class PriceDownScheduler {

    @Autowired
    private EventActivityConsumer activityConsumer;

    @Autowired
    private BookingClient bookingClient;

    //проверка каждый час
    @Scheduled(fixedRate = 3600000)
    public void checkForInactivity() {
        LocalDateTime now = LocalDateTime.now();
        Map<Long, EventStats> statsMap = activityConsumer.getStatsMap();

        for (Map.Entry<Long, EventStats> entry : statsMap.entrySet()) {
            Long eventId = entry.getKey();
            EventStats stats = entry.getValue();

            // сколько часов прошло с последней активности
            long hoursPassed = ChronoUnit.HOURS.between(stats.getLastActivityTime(), now);

            if (hoursPassed >= 24) {
                try {
                    bookingClient.updateEventPrice(eventId, BigDecimal.valueOf(-10));

                    // чтобы цена не падала каждый час, записываем что цена уже изменена
                    stats.registerActivity();

                    System.out.println("[Analytics] Цена на мероприятие " + eventId + " СНИЖЕНА на 10 руб. Из-за отсутствия спроса более 24 часов.");
                } catch (Exception e) {
                    System.err.println("Ошибка при автоматическом снижении цены: " + e.getMessage());
                }
            }
        }
    }
}