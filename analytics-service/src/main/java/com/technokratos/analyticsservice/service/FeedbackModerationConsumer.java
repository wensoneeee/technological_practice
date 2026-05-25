package com.technokratos.analyticsservice.service;

import com.technokratos.analyticsservice.client.BookingClient;
import com.technokratos.analyticsservice.dto.FeedbackModerationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackModerationConsumer {

    @Autowired
    private BookingClient bookingClient;

    private static final List<String> BLACKLIST = List.of("яблоко");

    @RabbitListener(queues = "feedback.moderation.queue")
    public void moderateFeedback(FeedbackModerationEvent event) {
        System.out.println("[Analytics] Получен новый отзыв на проверку контента ID: " + event.getFeedbackId());

        String textLowerCase = event.getText().toLowerCase();
        boolean isToxic = false;

        // проверка на запретные слова
        for (String badWord : BLACKLIST) {
            if (textLowerCase.contains(badWord)) {
                isToxic = true;
                break;
            }
        }

        // проверка на крик
        if (event.getText().equals(event.getText().toUpperCase()) && event.getText().length() > 5) {
            isToxic = true;
        }

        String finalStatus = isToxic ? "REJECTED" : "APPROVED";

        try {
            // возврат обратно
            bookingClient.updateFeedbackStatus(event.getFeedbackId(), finalStatus);
            System.out.println("[Analytics] Отзыв ID " + event.getFeedbackId() + " проверен. Вердикт: " + finalStatus);
        } catch (Exception e) {
            System.err.println("Не удалось отправить статус модерации через Feign: " + e.getMessage());
        }
    }
}