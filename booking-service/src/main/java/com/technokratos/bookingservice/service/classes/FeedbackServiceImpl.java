package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.config.RabbitConfig;
import com.technokratos.bookingservice.dto.event.FeedbackModerationEvent;
import com.technokratos.bookingservice.mapper.FeedbackMapper;
import com.technokratos.bookingservice.repository.jooq.PurchaseJooqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.technokratos.bookingservice.dto.dtos.FeedbackDto;
import com.technokratos.bookingservice.dto.dtos.FeedbackEventDto;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;
import com.technokratos.bookingservice.models.Feedback;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.jpa.EventRepository;
import com.technokratos.bookingservice.repository.jpa.FeedbackRepository;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
import com.technokratos.bookingservice.service.interfaces.FeedbackService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final FeedbackMapper feedbackMapper;
    private final PurchaseJooqRepository purchaseJooqRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public FeedbackDto getFeedbackByUserIdAndEventId(Long userId, Long eventId) {
        if (userId == null || eventId == null) return null;

        return feedbackRepository.findFeedbackByEventFeedback_EventIdAndUserFeedback_UserId(eventId, userId)
                .map(feedbackMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(value = "feedbacks", key = "#form.eventId")
    public void save(FeedbackForm form) {
        User user = userRepository.findById(form.getUserId()).orElseThrow();
        Event event = eventRepository.findById(form.getEventId()).orElseThrow();

        Feedback feedback = feedbackMapper.toEntity(form);

        feedback.setUserFeedback(user);
        feedback.setEventFeedback(event);
        feedback.setStatus("PENDING");
        feedback.setConfirmed(purchaseJooqRepository.didUserBoughtTicket(form.getEventId(), form.getUserId()));
        feedbackRepository.save(feedback);

        try {
            FeedbackModerationEvent fme = FeedbackModerationEvent.builder()
                    .feedbackId(feedback.getFeedbackId())
                    .text(feedback.getText())
                    .build();

            rabbitTemplate.convertAndSend("feedback.moderation.exchange",
                    "feedback.moderation.validate",
                    fme);
        } catch (Exception e) {
            throw new IllegalArgumentException("ошибка отправление в рэббит: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "feedbacks", key = "#eventId")
    public List<FeedbackEventDto> findCommentsByEventId(Long eventId) {
        return feedbackRepository.findFeedbackByEventFeedback_EventIdAndStatus(eventId, "APPROVED").stream()
                .map(feedbackMapper::toEventDto)
                .collect(Collectors.toList());
    }


    @Override
    public Double getAverageEventScore(Long eventId) {
        Double score = feedbackRepository.findAverageScore(eventId);
        if (score == null) {
            return 0.0;
        } else {
            return score;
        }
    }
}
