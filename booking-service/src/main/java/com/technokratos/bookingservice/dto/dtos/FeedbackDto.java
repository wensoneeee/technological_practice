package com.technokratos.bookingservice.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Feedback;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private Long userId;
    private Long eventId;
    private String text;
    private Integer score;
    private Boolean confirmed;
}

