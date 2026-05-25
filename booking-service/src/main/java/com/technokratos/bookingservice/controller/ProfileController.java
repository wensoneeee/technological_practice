package com.technokratos.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Web Profile UI", description = "Просмотр профиля")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Личный кабинет пользователя")
    @ApiResponse(responseCode = "200", description = "HTML-страница профиля",
            content = @Content(mediaType = "text/html"))
    public String profile(Model model, Principal principal) {
        model.addAttribute("user_data", userService.getUserByEmail(principal.getName()));
        return "profile_page";
    }

    // удален метод POST /profile с изменением пароля
    // booking-service больше не работает с паролями, это делает auth-service
    // В этом контроллере остается только просмотр профиля
}
