package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageValidator {
    public Validation validate(MultipartFile file) {
        Validation validation = new Validation();
        if (file == null || file.isEmpty() || file.getContentType() == null || !file.getContentType().split("/")[0].equals("image")) {
            validation.addError("файл должен быть картинкой!");
        }
        return validation;
    }
}
