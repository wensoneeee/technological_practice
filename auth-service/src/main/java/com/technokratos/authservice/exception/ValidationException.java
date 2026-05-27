package com.technokratos.authservice.exception;

import com.technokratos.authservice.validation.Validation;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final Validation validation;

    public ValidationException(Validation validation) {
        super("Ошибка валидации");
        this.validation = validation;
    }
}