package com.technokratos.analyticsservice.scheduler;

import com.technokratos.analyticsservice.client.BookingClient;
import com.technokratos.analyticsservice.model.EventStats;
import com.technokratos.analyticsservice.service.EventActivityConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PriceDownScheduler {

    private final EventActivityConsumer activityConsumer;

    private final BookingClient bookingClient;

    @Scheduled(fixedRate = 3600000)
    public void checkForInactivity() {
        LocalDateTime now = LocalDateTime.now();
        Map<Long, EventStats> statsMap = activityConsumer.getStatsMap();

        for (Map.Entry<Long, EventStats> entry : statsMap.entrySet()) {
            Long eventId = entry.getKey();
            EventStats stats = entry.getValue();

            long hoursPassed = ChronoUnit.HOURS.between(stats.getLastActivityTime(), now);

            if (hoursPassed >= 24) {
                try {
                    bookingClient.updateEventPrice(eventId, BigDecimal.valueOf(-10));
                    stats.registerActivity();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}