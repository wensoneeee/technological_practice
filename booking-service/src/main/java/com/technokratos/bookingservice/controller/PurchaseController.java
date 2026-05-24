package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.service.interfaces.PurchaseService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;

    @PostMapping("/purchase")
    public String purchase(Principal principal, RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        try{
            purchaseService.purchase(userId);
            redirectAttributes.addFlashAttribute("success", true);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/cart";
    }
}
