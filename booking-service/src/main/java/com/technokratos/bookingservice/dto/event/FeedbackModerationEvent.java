package com.technokratos.bookingservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackModerationEvent implements Serializable {
    private Long feedbackId;
    private String text;
}