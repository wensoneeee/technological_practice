package com.technokratos.bookingservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    @GetMapping("/login")
    public String login() {
        return "sign_in_page";
    }
}
