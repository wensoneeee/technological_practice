package com.technokratos.notificationservice.service;

import com.technokratos.notificationservice.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import com.technokratos.notificationservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationConsumer {

    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void consumeEmailEvent(EmailEvent event) {

        emailSenderService.sendEmail(event.getToEmail(), event.getSubject(), event.getText());
    }
}
