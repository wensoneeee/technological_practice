package com.technokratos.analyticsservice.service;

import com.technokratos.analyticsservice.client.BookingClient;
import com.technokratos.analyticsservice.config.RabbitConfig;
import com.technokratos.analyticsservice.dto.EventActivityEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EventActivityConsumer {

    @Autowired
    private BookingClient bookingClient;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleEventActivity(EventActivityEvent activityEvent) {
        System.out.println("Брокер в [analytics-service] поймал активность: "
                + activityEvent.getActivityType() + " для мероприятия ID: " + activityEvent.getEventId());

        long priceIncrease = 0;
        if ("VIEW".equals(activityEvent.getActivityType())) {
            priceIncrease = 10; // +10 рублей за просмотр страницы
        } else if ("CART".equals(activityEvent.getActivityType())) {
            priceIncrease = 50; // +50 рублей за добавление в корзину
        }

        if (priceIncrease > 0) {
            try {
                // Вызываем внутренний эндпоинт booking-service для изменения цены в БД
                bookingClient.updateEventPrice(activityEvent.getEventId(), BigDecimal.valueOf(priceIncrease));
                System.out.println("🔥 Цена на мероприятие " + activityEvent.getEventId() + " успешно повышена на " + priceIncrease + " руб. через Feign!");
            } catch (Exception e) {
                System.err.println("Ошибка при отправке обновления цены по Feign: " + e.getMessage());
            }
        }
    }
}