package com.technokratos.bookingservice.service.interfaces;

import com.technokratos.bookingservice.dto.dtos.FeedbackDto;
import com.technokratos.bookingservice.dto.dtos.FeedbackEventDto;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;
import com.technokratos.bookingservice.models.PurchaseItem;

import java.util.List;

public interface FeedbackService {
    public void save(FeedbackForm feedbackForm);
    public FeedbackDto getFeedbackByUserIdAndEventId(Long userId, Long eventId);
    List<FeedbackEventDto> findCommentsByEventId(Long eventId);

    void updateWasThere(List<PurchaseItem> purchaseItems, Long userId);

    Double getAverageEventScore(Long eventId);
}
