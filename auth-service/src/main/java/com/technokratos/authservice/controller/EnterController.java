package com.technokratos.authservice.controller;

import com.technokratos.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "Web Auth UI", description = "Отображение страниц авторизации и регистрации")
public class EnterController {

    private final AuthService authService;

    @GetMapping("/sign-in")
    @Operation(summary = "Получение страницы входа", description = "Возвращает HTML-страницу авторизации")
    @ApiResponse(responseCode = "200", description = "HTML-страницу загружена",
            content = @Content(mediaType = "text/html"))
    public String comeIn(Model model) {
        model.addAttribute("GATEWAY_URL", "http://localhost:8080");
        return "sign_in_page";
    }

    @GetMapping("/sign-up")
    @Operation(summary = "Получение страницы регистрации", description = "Возвращает HTML-страницу регистрации")
    @ApiResponse(responseCode = "200", description = "HTML-страница регистрации загружена",
            content = @Content(mediaType = "text/html"))
    public String goToSignUp(Model model) {
        model.addAttribute("GATEWAY_URL", "http://localhost:8080");
        return "sign_up_page";
    }
}