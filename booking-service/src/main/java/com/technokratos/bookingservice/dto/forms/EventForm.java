package com.technokratos.bookingservice.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventForm{
        Long id;
        String title;
        String description;
        LocalDateTime date;
        Integer availableTickets;
        BigDecimal price;
        String location;
}
