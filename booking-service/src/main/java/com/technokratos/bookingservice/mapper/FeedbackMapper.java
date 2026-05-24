package com.technokratos.bookingservice.mapper;

import com.technokratos.bookingservice.dto.dtos.FeedbackDto;
import com.technokratos.bookingservice.dto.dtos.FeedbackEventDto;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;
import com.technokratos.bookingservice.models.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "userFeedback.userId", target = "userId")
    @Mapping(source = "eventFeedback.eventId", target = "eventId")
    FeedbackDto toDto(Feedback feedback);

    @Mapping(source = "userFeedback.name", target = "userName")
    @Mapping(source = "createdAt", target = "date")
    @Mapping(source = "text", target = "comment")
    FeedbackEventDto toEventDto(Feedback feedback);

    @Mapping(target = "feedbackId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    @Mapping(target = "userFeedback", ignore = true)
    @Mapping(target = "eventFeedback", ignore = true)
    Feedback toEntity(FeedbackForm form);
}