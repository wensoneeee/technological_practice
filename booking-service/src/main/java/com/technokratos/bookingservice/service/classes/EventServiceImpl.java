package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.config.RabbitConfig;
import com.technokratos.bookingservice.dto.event.EventActivityEvent;
import com.technokratos.bookingservice.mapper.EventMapper;
import com.technokratos.bookingservice.repository.jooq.EventJooqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.dtos.EventDto;
import com.technokratos.bookingservice.dto.forms.EventForm;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.repository.jpa.CategoryRepository;
import com.technokratos.bookingservice.repository.jpa.EventRepository;
import com.technokratos.bookingservice.repository.jpa.ImageRepository;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.ImageService;

import java.math.BigDecimal;
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
    private final EventMapper eventMapper;
    private final EventJooqRepository eventJooqRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public EventDto save(EventForm eventForm) {
        Event event;

        if (eventForm.getId() != null && eventRepository.findById(eventForm.getId()).isPresent()) {
            event = eventRepository.findById(eventForm.getId()).get();
            eventMapper.updateEventFromForm(eventForm, event);
        } else {
            event = eventMapper.toEntity(eventForm);
            event.setImage(imageRepository.findById(1L).orElseThrow(IllegalArgumentException::new));
        }
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    @Cacheable(value = "topEvents")
    public List<EventDto> findAll() {
        List<Event> events = eventJooqRepository.findTopOrderBySalesForLast7Days();
        List<EventDto> eventDtos = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            EventDto eventDto = eventMapper.toDto(events.get(i));
            if (i < 3) {
                eventDto.setInWeeklyTop(true);
            }
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    @Override
    public EventDto findById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        try{
            EventActivityEvent eventActivityEvent = EventActivityEvent.builder()
                    .eventId(id)
                    .activityType("VIEW")
                    .build();
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, eventActivityEvent);
        }catch (Exception e){
            System.out.println("Не удалось отправить сообщение в Rabbit:"+e.getMessage());
        }

        return eventMapper.toDto(event);
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
        return eventRepository.findAllByCategories(category).stream().map(eventMapper::toDto).collect(Collectors.toList());
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

    @Override
    public void updateCost(Long eventId, BigDecimal cost){
        Event event = eventRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);
        event.setPrice(event.getPrice().add(cost));
        eventRepository.save(event);
    }
}
