package com.technokratos.bookingservice.mapper;

import com.technokratos.bookingservice.dto.dtos.CartItemDto;
import com.technokratos.bookingservice.dto.forms.CartItemForm;
import com.technokratos.bookingservice.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "eventCartItem.title", target = "eventName")
    @Mapping(source = "pricePerUnit", target = "price")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getPricePerUnit().multiply(new java.math.BigDecimal(cartItem.getQuantity())))")
    CartItemDto toDto(CartItem item);

    @Mapping(source = "price", target = "pricePerUnit")
    @Mapping(target = "cartItemId", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "eventCartItem", ignore = true)
    @Mapping(target = "userCartItem", ignore = true)
    CartItem toEntity(CartItemForm form);
}