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

        if(userForm.getEmail()==null || userForm.getEmail().isEmpty()){
            validation.addError("почта не может быть пустой");
        }
        if(userForm.getPassword()==null || userForm.getPassword().isEmpty() || userForm.getPassword().length()<8){
            validation.addError("длина пароля минимум 8 символов");
        }
        if(userForm.getName()==null || userForm.getName().isEmpty()){
            validation.addError("имя не может быть пустым");
        }
        if(!fromAdminPage && userRepository.findByEmail(userForm.getEmail()).isPresent()){
            validation.addError("такая почта уже существует");
        }
        return validation;
    }
}
