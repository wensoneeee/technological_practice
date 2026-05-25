package com.technokratos.authservice.controller;

import com.technokratos.authservice.dto.*;
import com.technokratos.authservice.jwt.JwtAccessTokenProvider;
import com.technokratos.authservice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody SignUpRequest request) {
        AuthResponse response = authService.signUp(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());

        Cookie cookie = new Cookie("JWT", authResponse.getAccessToken());
        cookie.setPath("/"); // Кука будет доступна для всех микросов
        cookie.setHttpOnly(true); // Защита от кражи токена через JavaScript (XSS)
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }
}
