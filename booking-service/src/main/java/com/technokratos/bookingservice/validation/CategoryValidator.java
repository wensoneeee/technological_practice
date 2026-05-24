package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.CategoryForm;

@Component
public class CategoryValidator {

    public Validation validate(CategoryForm form) {
        Validation result = new Validation();

        if (form.getName() == null || form.getName().trim().isEmpty()) {
            result.addError("Название категории не может быть пустым");
        }

        if (form.getDescription() == null || form.getDescription().trim().isEmpty()) {
            result.addError("Описание категории не может быть пустым");
        }

        return result;
    }
}