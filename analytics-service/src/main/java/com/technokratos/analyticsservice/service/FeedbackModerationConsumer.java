package com.technokratos.analyticsservice.service;

import com.technokratos.analyticsservice.dto.FeedbackModerationEvent;
import com.technokratos.analyticsservice.client.BookingClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FeedbackModerationConsumer {

    @Autowired
    private BookingClient bookingClient;

    private static final List<String> BLACKLIST = List.of("яндекс", "афиша", "отстой", "мошенники", "плохо");

    @RabbitListener(queues = "feedback.moderation.queue")
    public void moderateFeedback(FeedbackModerationEvent event) {
        if (event == null || event.getFeedbackId() == null || event.getText() == null) {
            return;
        }

        String textLowerCase = event.getText().toLowerCase();
        boolean isToxic = false;

        for (String badWord : BLACKLIST) {
            if (textLowerCase.contains(badWord)) {
                isToxic = true;
                break;
            }
        }

        if (event.getText().equals(event.getText().toUpperCase()) && event.getText().length() > 5) {
            isToxic = true;
        }

        String finalStatus = isToxic ? "REJECTED" : "APPROVED";

        try {
            bookingClient.updateFeedbackStatus(event.getFeedbackId(), finalStatus);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}