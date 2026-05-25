package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User userFeedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event eventFeedback;

    private String text;
    private String status;

    @Range(min = 1, max = 5)
    private Integer score;

    @CreationTimestamp
    private Timestamp createdAt;

    private Boolean confirmed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return feedbackId != null && feedbackId.equals(feedback.feedbackId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("Feedback(")
                .append("feedbackId=").append(feedbackId)
                .append(", text=").append(text)
                .append(", score=").append(score)
                .append(", createdAt=").append(createdAt)
                .append(", confirmed=").append(confirmed)
                .append(')').toString();
    }
}
