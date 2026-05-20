package com.technokratos.bookingservice.service.interfaces;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.technokratos.bookingservice.dto.forms.PasswordForm;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.dto.forms.UserForm;

import java.util.List;

public interface UserService {
    void createUser(UserForm user);
    List<UserDto> getAllUsers();
    void saveUser(UserForm user);
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email) throws UsernameNotFoundException;
    void changePassword(PasswordForm passwordForm, String email);
    void changeProfileImage(Long imageId, String email);
    void deleteProfileImage(String email);
    void setConfirmed(String email);
}
