package com.technokratos.bookingservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.UserForm;
import com.technokratos.bookingservice.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public Validation validate(UserForm userForm, boolean fromAdminPage) {
        Validation validation = new Validation();

        if(userForm.email()==null || userForm.email().isEmpty()){
            validation.addError("почта не может быть пустой");
        }
        if(userForm.password()==null || userForm.password().isEmpty() || userForm.password().length()<8){
            validation.addError("длина пароля минимум 8 символов");
        }
        if(userForm.name()==null || userForm.name().isEmpty()){
            validation.addError("имя не может быть пустым");
        }
        if(!fromAdminPage && userRepository.findByEmail(userForm.email()).isPresent()){
            validation.addError("такая почта уже существует");
        }
        return validation;
    }
}
