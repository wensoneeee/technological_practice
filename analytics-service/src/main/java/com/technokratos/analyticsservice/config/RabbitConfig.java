package com.technokratos.analyticsservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "event.activity.queue";
    public static final String FEEDBACK_QUEUE = "feedback.moderation.queue";

    @Bean
    public Queue activityQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue feedbackQueue() {
        return new Queue(FEEDBACK_QUEUE, true); // Теперь analytics-service сам создаст её, если брокер пустой!
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}