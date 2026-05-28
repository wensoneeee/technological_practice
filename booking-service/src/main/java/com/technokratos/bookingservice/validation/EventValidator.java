package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.EventForm;

import java.math.BigDecimal;

@Component
public class EventValidator {

    public Validation validate(EventForm form) {
        Validation result = new Validation();

        if (form.getTitle() == null || form.getTitle().trim().isEmpty()) {
            result.addError("Заголовок не может быть пустым");
        } else if (form.getTitle().length() < 3 || form.getTitle().length() > 100) {
            result.addError("Заголовок должен быть от 3 до 100 символов");
        }

        if (form.getDescription() == null || form.getDescription().trim().isEmpty()) {
            result.addError("Описание не может быть пустым");
        }

        if (form.getDate() == null) {
            result.addError("Дата проведения обязательна");
        }

        if (form.getAvailableTickets() == null || form.getAvailableTickets() < 0) {
            result.addError("количество билетов не может быть отрицательным");
        }

        if (form.getPrice() == null || form.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            result.addError("Цена не может быть отрицательной");
        }

        if (form.getLocation() == null || form.getLocation().trim().isEmpty()) {
            result.addError("Место проведения обязательно");
        }

        return result;
    }
}