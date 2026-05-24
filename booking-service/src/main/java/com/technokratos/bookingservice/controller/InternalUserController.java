package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.models.Role;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> createProfile(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("role") Role role){
        User user = User.builder()
                .email(email)
                .name(name)
                .role(role)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
