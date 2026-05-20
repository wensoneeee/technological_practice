package com.technokratos.bookingservice.dto.dtos;

import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Feedback;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FeedbackDto {
    private Long userId;
    private Long eventId;
    private String text;
    private Integer score;
    private Boolean confirmed;

    public static FeedbackDto of(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        return FeedbackDto.builder()
                .userId(feedback.getUserFeedback().getUserId())
                .eventId(feedback.getEventFeedback().getEventId())
                .text(feedback.getText())
                .score(feedback.getScore())
                .confirmed(feedback.getConfirmed())
                .build();
    }

    public static List<FeedbackDto> from(List<Feedback> feedbacks) {
        return feedbacks.stream().map(FeedbackDto::of).collect(Collectors.toList());
    }
}

