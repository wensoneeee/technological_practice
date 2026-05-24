package com.technokratos.bookingservice.mapper;

import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.dto.forms.UserForm;
import com.technokratos.bookingservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "image.imageId", target = "imageId")
    UserDto toDto(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    @Mapping(target = "confirmCode", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    User toEntity(UserForm form);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    @Mapping(target = "confirmCode", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    void updateUserFromForm(UserForm form, @MappingTarget User user);
}