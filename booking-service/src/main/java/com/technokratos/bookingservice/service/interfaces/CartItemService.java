package com.technokratos.bookingservice.service.interfaces;

import com.technokratos.bookingservice.dto.dtos.CartItemDto;
import com.technokratos.bookingservice.dto.forms.CartItemForm;
import com.technokratos.bookingservice.models.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemService {
    CartItem save(CartItemForm cartItemForm);

    List<CartItemDto> getByUserId(Long userId);

    BigDecimal getTotalPrice(Long userId);

    void emptyCartForPurchase(Long userId);

    void deleteAllByUserId(Long userId);

    void deleteCartItem(Long id);
}
