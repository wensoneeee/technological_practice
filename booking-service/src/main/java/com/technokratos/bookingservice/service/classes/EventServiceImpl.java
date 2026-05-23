package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.dtos.EventDto;
import com.technokratos.bookingservice.dto.forms.EventForm;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.repository.CategoryRepository;
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.repository.ImageRepository;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public EventDto save(EventForm eventForm) {
        Event event;

        if (eventForm.id() != null && eventRepository.findById(eventForm.id()).isPresent()) {
            event = eventRepository.findById(eventForm.id()).get();
            event.setTitle(eventForm.title());
            event.setDescription(eventForm.description());
            event.setPrice(eventForm.price());
            event.setAvailableTickets(eventForm.availableTickets());
            event.setLocation(eventForm.location());
            event.setTimestamp(eventForm.date());
        } else {
            event = Event.builder()
                    .title(eventForm.title())
                    .description(eventForm.description())
                    .price(eventForm.price())
                    .availableTickets(eventForm.availableTickets())
                    .location(eventForm.location())
                    .timestamp(eventForm.date())
                    .image(imageRepository.findById(1L).orElseThrow(IllegalArgumentException::new))
                    .build();
        }
        eventRepository.save(event);
        return EventDto.of(event);
    }

    @Override
    public List<EventDto> findAll() {
        List<Event> events = eventRepository.findTopOrderBySalesForLast7Days();
        List<EventDto> eventDtos = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            EventDto eventDto = EventDto.of(events.get(i));
            if (i < 3) {
                eventDto.setInWeeklyTop(true);
            }
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    @Override
    public EventDto findById(Long id) {
        return EventDto.of(eventRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public void delete(Long id) {
        Long imageId = eventRepository.findById(id).orElseThrow(IllegalArgumentException::new).getImage().getImageId();
        eventRepository.deleteById(id);
        imageService.clearImageIfNotBeingUsed(imageId);
    }

    @Override
    public List<EventDto> findByCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        return eventRepository.findAllByCategories(category).stream().map(EventDto::of).collect(Collectors.toList());
    }

    @Override
    public void updateAvailableTickets(List<PurchaseItem> purchaseItems) {
        for (PurchaseItem purchaseItem : purchaseItems) {
            //делаем так ибо иначе может быть неверная информация(пусть этот способ и не очень эффективный)
            Event event = eventRepository.findById(purchaseItem.getEvent().getEventId()).orElseThrow(IllegalArgumentException::new);
            event.setAvailableTickets(event.getAvailableTickets() - purchaseItem.getQuantity());
            if (event.getAvailableTickets() < 0) {
                throw new IllegalArgumentException("вы не можете купить билетов больше, чем доступно");
            }
            eventRepository.save(event);
        }
    }

    @Override
    public void changeEventPhoto(Long photoId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);
        Long oldImageId = event.getImage().getImageId();
        event.setImage(imageRepository.findById(photoId).orElseThrow(IllegalArgumentException::new));
        eventRepository.save(event);
        imageService.clearImageIfNotBeingUsed(oldImageId);
    }
}
