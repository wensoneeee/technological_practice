package com.technokratos.authservice.controller;

import com.technokratos.authservice.dto.AuthRequest;
import com.technokratos.authservice.dto.AuthResponse;
import com.technokratos.authservice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class EnterController {

    private final AuthService authService;

    @GetMapping("/api/v1/auth/come-in")
    public String comeIn(Model model) {
        model.addAttribute("GATEWAY_URL", "http://localhost:8080");
        return "auth_page";
    }
}
