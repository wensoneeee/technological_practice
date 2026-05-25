package com.technokratos.authservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnterController {
    @GetMapping("/api/v1/auth/come-in")
    public String comeIn(Model model) {
        model.addAttribute("GATEWAY_URL", "http://localhost:8080");
        return "auth_page";
    }

}
