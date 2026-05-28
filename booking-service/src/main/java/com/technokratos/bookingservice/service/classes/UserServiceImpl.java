package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.jpa.ImageRepository;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
import com.technokratos.bookingservice.service.interfaces.ImageService;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
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

}
















