package com.technokratos.bookingservice.dto.forms;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackForm {
    Long userId;
    Long eventId;
    String text;
    Integer score;
}
