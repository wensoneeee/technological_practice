package com.technokratos.bookingservice.mapper;

import com.technokratos.bookingservice.dto.dtos.EventDto;
import com.technokratos.bookingservice.dto.forms.EventForm;
import com.technokratos.bookingservice.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "eventId", target = "id")
    @Mapping(source = "image.imageId", target = "imageId")
    @Mapping(target = "inWeeklyTop", ignore = true)
    EventDto toDto(Event event);

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "date", target = "timestamp")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "purchaseItems", ignore = true)
    Event toEntity(EventForm form);

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "date", target = "timestamp")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "purchaseItems", ignore = true)
    void updateEventFromForm(EventForm form, @MappingTarget Event event);
}