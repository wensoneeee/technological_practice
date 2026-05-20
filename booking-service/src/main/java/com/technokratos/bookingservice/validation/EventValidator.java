package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.EventForm;

import java.math.BigDecimal;

@Component
public class EventValidator {

    public Validation validate(EventForm form) {
        Validation result = new Validation();

        if (form.title() == null || form.title().trim().isEmpty()) {
            result.addError("Заголовок не может быть пустым");
        } else if (form.title().length() < 3 || form.title().length() > 100) {
            result.addError("Заголовок должен быть от 3 до 100 символов");
        }

        if (form.description() == null || form.description().trim().isEmpty()) {
            result.addError("Описание не может быть пустым");
        }

        if (form.date() == null) {
            result.addError("Дата проведения обязательна");
        }

        if (form.availableTickets() == null || form.availableTickets() < 0) {
            result.addError("rоличество билетов не может быть отрицательным");
        }

        if (form.price() == null || form.price().compareTo(BigDecimal.ZERO) < 0) {
            result.addError("Цена не может быть отрицательной");
        }

        if (form.location() == null || form.location().trim().isEmpty()) {
            result.addError("Место проведения обязательно");
        }

        return result;
    }
}