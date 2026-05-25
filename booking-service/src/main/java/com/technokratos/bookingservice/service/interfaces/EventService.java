package com.technokratos.bookingservice.service.interfaces;

import com.technokratos.bookingservice.dto.dtos.EventDto;
import com.technokratos.bookingservice.dto.forms.EventForm;
import com.technokratos.bookingservice.models.PurchaseItem;

import java.math.BigDecimal;
import java.util.List;

public interface EventService {
    public EventDto save(EventForm eventForm);
    public List<EventDto> findAll();
    public EventDto findById(Long id);
    public void delete(Long id);
    List<EventDto> findByCategory(String categoryName);
    void updateAvailableTickets(List<PurchaseItem> purchaseItems);

    void changeEventPhoto(Long photoId, Long eventId);

    void updateCost(Long eventId, BigDecimal cost);
}
