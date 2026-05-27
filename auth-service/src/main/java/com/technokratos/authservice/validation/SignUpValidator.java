package com.technokratos.authservice.validation;

import com.technokratos.authservice.dto.SignUpRequest;
import com.technokratos.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpValidator {

    private final UserRepository userRepository;

    public Validation validate(SignUpRequest request) {
        Validation validation = new Validation();

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            validation.addError("Почта не может быть пустой");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().length() < 8) {
            validation.addError("Длина пароля должна быть минимум 8 символов");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            validation.addError("Имя не может быть пустым");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            validation.addError("Пользователь с такой почтой уже существует");
        }

        return validation;
    }
}