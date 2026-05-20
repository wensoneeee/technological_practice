package com.technokratos.bookingservice.dto.dtos;

import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Event;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
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


    public static EventDto of(Event event) {
        return EventDto.builder()
                .id(event.getEventId())
                .title(event.getTitle())
                .timestamp(String.valueOf(event.getTimestamp()))
                .description(event.getDescription())
                .location(event.getLocation())
                .price(String.valueOf(event.getPrice()))
                .availableTickets(String.valueOf(event.getAvailableTickets()))
                .imageId(event.getImage().getImageId())
                .build();
    }

    public static List<EventDto> from(List<Event> events){
        return events.stream()
                .map(EventDto::of)
                .collect(Collectors.toList());
    }
}
