package com.technokratos.authservice.controller;

import com.technokratos.authservice.dto.*;
import com.technokratos.authservice.exception.ValidationException;
import com.technokratos.authservice.service.AuthService;
import com.technokratos.authservice.validation.SignUpValidator;
import com.technokratos.authservice.validation.Validation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Методы аутентификации и регистрации")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SignUpValidator signUpValidator;

    @PostMapping("/sign-up")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Принимает данные нового аккаунта, создает запись в базе данных и возвращает пару JWT-токенов."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные или email уже занят")
    })
    public ResponseEntity<AuthResponse> register(@RequestBody SignUpRequest request) {
        Validation validation = signUpValidator.validate(request);

        if (validation.hasErrors()) {
            throw new ValidationException(validation);
        }

        AuthResponse response = authService.signUp(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация (Вход в систему)",
            description = "Проверяет email и пароль. Возвращает JWT-токены в теле ответа, а также устанавливает Access Token в защищенную куку HttpOnly."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход в систему",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
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
    @Operation(
            summary = "Выход из системы (Logout)",
            description = "Удаляет сессию пользователя, инвалидирует его Refresh-токен."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел из системы"),
            @ApiResponse(responseCode = "400", description = "Передан невалидный или отсутствующий Refresh-токен")
    })
    @Deprecated
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }


    @GetMapping("/logout")
    @Operation(
            summary = "Выход из системы (Logout)",
            description = "Удаляет сессию пользователя, инвалидирует его Refresh-токен."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел из системы"),
            @ApiResponse(responseCode = "400", description = "Передан невалидный или отсутствующий Refresh-токен")
    })
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
