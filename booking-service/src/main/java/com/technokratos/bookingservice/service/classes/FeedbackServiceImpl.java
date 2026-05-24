package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
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
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.repository.FeedbackRepository;
import com.technokratos.bookingservice.repository.PurchaseRepository;
import com.technokratos.bookingservice.repository.UserRepository;
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
    private final PurchaseRepository purchaseRepository;
    private final FeedbackMapper feedbackMapper;

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
        feedback.setConfirmed(purchaseRepository.didUserBoughtTicket(form.getEventId(), form.getUserId()));
        feedbackRepository.save(feedback);
    }

    @Override
    @Cacheable(value = "feedbacks", key = "#eventId")
    public List<FeedbackEventDto> findCommentsByEventId(Long eventId) {
        return feedbackRepository.findFeedbacksByEventFeedback_EventId(eventId).stream()
                .map(feedbackMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateWasThere(List<PurchaseItem> purchaseItems, Long userId) {
        for (PurchaseItem purchaseItem : purchaseItems) {
            Optional<Feedback> feedback = feedbackRepository.findFeedbackByEventFeedback_EventIdAndUserFeedback_UserId(purchaseItem.getEvent().getEventId(), userId);
            if (feedback.isPresent()) {
                feedback.get().setConfirmed(true);
                feedbackRepository.save(feedback.get());
            }
        }
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
