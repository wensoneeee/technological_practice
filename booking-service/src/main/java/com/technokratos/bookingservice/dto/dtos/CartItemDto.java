package com.technokratos.bookingservice.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.CartItem;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private String eventName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
