package com.technokratos.bookingservice.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Event;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String timestamp;
    private String location;
    private Long imageId;
    private String availableTickets;
    private String price;
    public boolean inWeeklyTop = false;
}
