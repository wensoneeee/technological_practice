package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.repository.UserRepository;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        return UserDto.of(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserDto.from(userRepository.findAll());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return UserDto.of(userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new));
    }

    // Примечание: Метод сохранения/апдейта профиля можно добавить позже,
    // когда настроим связь между микросервисами
}
















