package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Feedback;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findFeedbackByEventFeedback_EventIdAndUserFeedback_UserId(Long eventId, Long userId);
    List<Feedback> findFeedbackByEventFeedback_EventIdAndStatus(Long eventFeedbackEventId, String status);

    @Query("select avg(score) from Feedback " +
            "where eventFeedback.eventId=:eventFeedbackEventId and status='APPROVED'")
    Double findAverageScore(Long eventFeedbackEventId);
}
