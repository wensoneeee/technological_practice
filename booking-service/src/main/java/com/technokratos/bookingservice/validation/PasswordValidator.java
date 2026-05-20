package com.technokratos.bookingservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.PasswordForm;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class PasswordValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Validation validate(PasswordForm passwordForm, String email) {
        Validation validation = new Validation();
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);

        if(passwordForm.oldPassword().isEmpty() || passwordForm.newPassword().isEmpty() || passwordForm.confirmPassword().isEmpty()) {
            validation.addError("никакое из полей не может быть пустым");
            return validation;
        }


        if(passwordForm.oldPassword().equals(passwordForm.newPassword())){
            validation.addError("старый и новые пароли не могут совпадать");
        }
        else if(passwordEncoder.matches(passwordForm.oldPassword(), user.getPassword())){

            if(passwordForm.newPassword().equals(passwordForm.confirmPassword())){

                if(passwordForm.newPassword().length()<8){
                    validation.addError("пароль должен быть больше 8 символов!");
                }
            }else{
                validation.addError("пароли не совпадают");
            }
        }else {
            validation.addError("старый пароль неверен");
        }
        return validation;
    }
}