package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.forms.PasswordForm;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.dto.forms.UserForm;
import com.technokratos.bookingservice.models.Role;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.ImageRepository;
import com.technokratos.bookingservice.repository.UserRepository;
import com.technokratos.bookingservice.service.interfaces.ImageService;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public void saveUser(UserForm user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.email());
        if (optionalUser.isPresent()) {
            User userFromDB = optionalUser.get();
            userMapper.updateUserFromForm(user, userFromDB);
            userFromDB.setPassword(passwordEncoder.encode(user.password()));
            userRepository.save(userFromDB);
        } else {
            User userDb = userMapper.toEntity(user);
            userDb.setPassword(passwordEncoder.encode(user.password()));
            userDb.setImage(imageRepository.findById(1L).orElse(null));
            userDb.setConfirmed("CONFIRMED");
            userDb.setConfirmCode(UUID.randomUUID().toString());
            userRepository.save(userDb);
        }
    }

    @Override
    public void createUser(UserForm userForm) {
        User user = userMapper.toEntity(userForm);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userForm.password()));
        user.setImage(imageRepository.findById(1L).orElse(null));
        user.setConfirmed("NOT_CONFIRMED");
        user.setConfirmCode(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public void changePassword(PasswordForm passwordForm, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        user.setPassword(passwordEncoder.encode(passwordForm.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void changeProfileImage(Long imageId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        user.setImage(imageRepository.findById(imageId).orElseThrow(IllegalArgumentException::new));
        userRepository.save(user);
    }

    @Override
    public void deleteProfileImage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        Long imageId = user.getImage().getImageId();
        user.setImage(imageRepository.findById(1L).orElseThrow(IllegalArgumentException::new));
        imageService.deleteImage(imageId);
        userRepository.save(user);
    }

    @Override
    public void setConfirmed(String code) {
        User user = userRepository.findByConfirmCode(code);
        user.setConfirmed("CONFIRMED");
        userRepository.save(user);
    }
}
















