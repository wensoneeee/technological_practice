package com.technokratos.analyticsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "booking-service", url = "http://booking-service:8082")
public interface BookingClient {

    @PutMapping("/api/v1/internal/events/{eventId}/price")
    void updateEventPrice(@PathVariable("eventId") Long eventId, @RequestParam("amount") BigDecimal amount);

    @PutMapping("/api/v1/internal/feedbacks/{feedbackId}/status")
    void updateFeedbackStatus(@PathVariable("feedbackId") Long feedbackId, @RequestParam("status") String status);
}