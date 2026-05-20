package com.technokratos.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.UserForm;
import com.technokratos.bookingservice.service.interfaces.UserService;
import com.technokratos.bookingservice.validation.UserValidator;
import com.technokratos.bookingservice.validation.Validation;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final UserValidator userValidator;

    @PostMapping("/register")
    public String signup(@ModelAttribute UserForm userForm, RedirectAttributes redirectAttributes) {
        Validation validation = userValidator.validate(userForm, false);

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/register";
        }else{
            userService.createUser(userForm);
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String getSignUpPage() {
        return "sign_up_page";
    }

}













