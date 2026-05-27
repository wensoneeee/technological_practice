package com.technokratos.authservice.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Validation {
    private List<String> errors = new ArrayList<>();

    public void addError(String error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
