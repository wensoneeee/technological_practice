package com.technokratos.bookingservice.dto.forms;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventForm(
        Long id,
        String title,
        String description,
        LocalDateTime date,
        Integer availableTickets,
        BigDecimal price,
        String location
) {}
