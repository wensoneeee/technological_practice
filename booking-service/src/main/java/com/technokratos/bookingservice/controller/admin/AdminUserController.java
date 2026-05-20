package com.technokratos.bookingservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.UserForm;
import com.technokratos.bookingservice.service.interfaces.UserService;
import com.technokratos.bookingservice.validation.UserValidator;
import com.technokratos.bookingservice.validation.Validation;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping("/admin/user")
    public String adminUser(@RequestParam(required = false) Long userId, Model model) {
        model.addAttribute("users", userService.getAllUsers());

        if(userId != null) {
            model.addAttribute("selectedUser", userService.getUserById(userId));
        }
        return "admin/admin_user_page";
    }

    @PostMapping("/admin/user/save")
    public String saveUser(@ModelAttribute("user") UserForm user, RedirectAttributes redirectAttributes) {
        Validation validation = userValidator.validate(user, true);

        if(validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else {
            userService.saveUser(user);
        }
        return "redirect:/admin/user";
    }
}
