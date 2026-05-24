package com.technokratos.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

    private final UserService userService;


    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal != null) {
            UserDto user = userService.getUserByEmail(principal.getName());
            model.addAttribute("userRole", user.getRole());
            model.addAttribute("userName", user.getName());
        }
    }
}