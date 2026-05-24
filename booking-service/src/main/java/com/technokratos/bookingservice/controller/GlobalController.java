package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.security.Principal;

//Вместо Principal мы проверяем, есть ли в текущем потоке ID пользователя
// Если есть (шлюз нас пропустил и прислал заголовок) — достаем данные пользователя
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

    private final UserService userService;
    private final UserContext userContext;

    @ModelAttribute
    public void addAttributes(Model model) {
        Long userId = userContext.getUserId();
        String userRole = userContext.getUserRole();

        if (userId != null) {
            model.addAttribute("userRole", userRole);
            UserDto user = userService.getUserById(userId);
            // и кондр говорил что афоритис в заголовке передавать плохо (зачееееееем???)
            if (user != null) {
                model.addAttribute("userName", user.getName());
            }
        }
    }
}