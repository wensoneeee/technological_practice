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
}
