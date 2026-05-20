package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.CartItemForm;

import java.math.BigDecimal;

@Component
public class CartItemValidator {

    public Validation validate(CartItemForm form) {
        Validation result = new Validation();

        if (form.getUserId() == null) {
            result.addError("пользователь не может быть пустым");
        }

        if (form.getEventId() == null) {
            result.addError("событие не может быть пустым");
        }

        if (form.getQuantity() == null || form.getQuantity() <= 0) {
            result.addError("Количество должно быть больше нуля");
        }

        if (form.getPrice() == null || form.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            result.addError("Цена не может быть пустой или отрицательной");
        }

        return result;
    }
}