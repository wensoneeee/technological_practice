package com.technokratos.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.PasswordForm;
import com.technokratos.bookingservice.service.interfaces.UserService;
import com.technokratos.bookingservice.validation.PasswordValidator;
import com.technokratos.bookingservice.validation.Validation;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final PasswordValidator passwordValidator;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user_data", userService.getUserByEmail(principal.getName()));
        return "profile_page";
    }

    @PostMapping("/profile")
    public String changeProfile(@ModelAttribute PasswordForm passwordForm, Principal principal, RedirectAttributes redirectAttributes) {
        Validation validation = passwordValidator.validate(passwordForm, principal.getName());
        if(validation.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else{
            userService.changePassword(passwordForm, principal.getName());
        }
        return "redirect:/profile";
    }
}
