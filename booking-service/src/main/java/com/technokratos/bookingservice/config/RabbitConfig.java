package com.technokratos.bookingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "event.activity.exchange";
    public static final String QUEUE_NAME = "event.activity.queue";
    public static final String ROUTING_KEY = "event.activity.click";

    public static final String FEEDBACK_EXCHANGE = "feedback.moderation.exchange";
    public static final String FEEDBACK_QUEUE = "feedback.moderation.queue";
    public static final String FEEDBACK_ROUTING_KEY = "feedback.moderation.validate";

    @Bean
    public TopicExchange activityExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue activityQueue() { return new Queue("event.activity.queue", true); }

    @Bean
    public Queue feedbackQueue() { return new Queue("feedback.moderation.queue", true); }

    // Аргументы должны называться ИМЕННО activityQueue и activityExchange
    @Bean
    public Binding activityBinding(Queue activityQueue, TopicExchange activityExchange) {
        return BindingBuilder.bind(activityQueue).to(activityExchange).with("event.activity.click");
    }

    // Аргументы должны называться ИМЕННО feedbackQueue и feedbackExchange
    @Bean
    public Binding feedbackBinding(Queue feedbackQueue, TopicExchange feedbackExchange) {
        return BindingBuilder.bind(feedbackQueue).to(feedbackExchange).with("feedback.moderation.validate");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public TopicExchange feedbackExchange() {
        return new TopicExchange(FEEDBACK_EXCHANGE);
    }
}