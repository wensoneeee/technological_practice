package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class InternalEventController {

    private final EventService eventService;

    @PutMapping("/api/v1/internal/events/{eventId}/price")
    @Transactional
    public ResponseEntity<Void> increaseEventPrice(@PathVariable Long eventId, @RequestParam BigDecimal amount) {
        eventService.updateCost(eventId, amount);
        return ResponseEntity.ok().build();
    }
}
