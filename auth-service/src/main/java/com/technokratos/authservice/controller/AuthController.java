package com.technokratos.authservice.controller;

import com.technokratos.authservice.dto.*;
import com.technokratos.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody SignUpRequest request) {
        AuthResponse response = authService.signUp(
                request.getEmail(),
                request.getPassword(),
                request.getFullName()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        AuthResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }

    @GetMapping("/validate")
    public ResponseEntity<AccountResponse> validate(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateAndGetAccount(token));
    }
}
