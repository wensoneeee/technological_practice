package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.service.interfaces.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Tag(name = "Internal Event Controller",
        description = "Внутренний API для автоматического управления событиями (вызовы от шедулеров и аналитики)")
@RequiredArgsConstructor
public class InternalEventController {

    private final EventService eventService;

    @PutMapping("/api/v1/internal/events/{eventId}/price")
    @Operation(
            summary = "Изменение стоимости события",
            description = "Вызывается внутренними планировщиками или сервисом аналитики для динамического изменения цены билетов."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Стоимость события успешно изменена"),
            @ApiResponse(responseCode = "404", description = "Событие с указанным ID не найдено")
    })
    @Transactional
    public ResponseEntity<Void> increaseEventPrice(@PathVariable Long eventId, @RequestParam BigDecimal amount) {
        eventService.updateCost(eventId, amount);
        return ResponseEntity.ok().build();
    }
}
