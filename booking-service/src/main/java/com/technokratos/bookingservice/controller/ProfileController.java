package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final UserContext userContext;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user_data", userService.getUserByEmail(principal.getName()));
        return "profile_page";
    }

    // удален метод POST /profile с изменением пароля
    // booking-service больше не работает с паролями, это делает auth-service
    // В этом контроллере остается только просмотр профиля
}
