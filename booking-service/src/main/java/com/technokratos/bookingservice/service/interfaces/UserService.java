package com.technokratos.bookingservice.service.interfaces;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.technokratos.bookingservice.dto.forms.PasswordForm;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.dto.forms.UserForm;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email) throws UsernameNotFoundException;

    void changeProfileImage(Long imageId, String email);

    void deleteProfileImage(String email);
}
