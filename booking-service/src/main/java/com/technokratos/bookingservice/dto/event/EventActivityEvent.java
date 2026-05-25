package com.technokratos.bookingservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventActivityEvent {
    private Long eventId;
    private String activityType;
}