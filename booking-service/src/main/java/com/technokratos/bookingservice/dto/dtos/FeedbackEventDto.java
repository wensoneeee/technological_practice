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

}
