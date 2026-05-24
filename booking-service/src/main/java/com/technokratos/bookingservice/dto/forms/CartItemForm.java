package com.technokratos.bookingservice.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemForm {
    private Long userId;
    private Long eventId;
    private BigDecimal price;
    private Integer quantity;
}
