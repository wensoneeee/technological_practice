package com.technokratos.bookingservice.service.classes;

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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    @Override
    public UserDto getUserById(Long id) {
        return UserDto.of(userRepository.findById(id).orElse(null));
    }

    @Override
    public void saveUser(UserForm user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.email());
        if (optionalUser.isPresent()) {
            User userFromDB = optionalUser.get();
            userFromDB.setName(user.name());
            userFromDB.setPassword(passwordEncoder.encode(user.password()));
            userFromDB.setEmail(user.email());
            userFromDB.setRole(user.role());
            userRepository.save(userFromDB);
        } else {
            User userDb = User.builder()
                    .email(user.email())
                    .password(passwordEncoder.encode(user.password()))
                    .name(user.name())
                    .role(user.role())
                    .image(imageRepository.findById(1L).orElse(null))
                    .confirmed("CONFIRMED")
                    .confirmCode(UUID.randomUUID().toString())
                    .build();
            userRepository.save(userDb);
        }
    }

    @Override
    public void createUser(UserForm userForm) {
        User user = User.builder()
                .email(userForm.email())
                .password(passwordEncoder.encode(userForm.password()))
                .name(userForm.name())
                .role(Role.USER)
                .image(imageRepository.findById(1L).orElse(null))
                .confirmed("NOT_CONFIRMED")
                .confirmCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserDto.from(userRepository.findAll());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return UserDto.of(userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new));
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
















