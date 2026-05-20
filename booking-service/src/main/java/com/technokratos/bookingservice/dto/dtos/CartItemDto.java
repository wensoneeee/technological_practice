package com.technokratos.bookingservice.dto.dtos;

import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.CartItem;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemDto {
    private String eventName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public static CartItemDto of(CartItem cartItem, String eventName) {
        return CartItemDto.builder()
                .eventName(eventName)
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPricePerUnit())
                .totalPrice(cartItem.getPricePerUnit().multiply(new BigDecimal(cartItem.getQuantity())))
                .build();
    }
}
