package com.technokratos.bookingservice.dto.forms;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemForm {
    private Long userId;
    private Long eventId;
    private BigDecimal price;
    private Integer quantity;
}
