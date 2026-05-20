package com.technokratos.bookingservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.technokratos.bookingservice.service.interfaces.UserService;

@Controller
public class ConfirmController {

    private final UserService userService;

    public ConfirmController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/confirm/{code}")
    public String confirm(@PathVariable String code) {
        userService.setConfirmed(code);
        return "redirect:/login";
    }
}
