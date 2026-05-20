package com.technokratos.bookingservice.dto.dtos;

import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Feedback;

@Data
@Builder
public class FeedbackEventDto {
    private String userName;
    private String date;
    private String comment;
    private Integer score;
    private Boolean confirmed;

    public static FeedbackEventDto of(Feedback feedback, String name) {
        return FeedbackEventDto.builder()
                .userName(name)
                .date(String.valueOf(feedback.getCreatedAt()))
                .comment(feedback.getText())
                .score(feedback.getScore())
                .confirmed(feedback.getConfirmed())
                .build();
    }
}
