package com.technokratos.bookingservice.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackForm {
    Long userId;
    Long eventId;
    String text;
    Integer score;
}
