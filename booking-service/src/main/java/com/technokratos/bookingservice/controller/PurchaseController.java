package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.service.interfaces.PurchaseService;
import com.technokratos.bookingservice.service.interfaces.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserContext userContext;

    @PostMapping("/purchase")
    public String purchase(RedirectAttributes redirectAttributes) {
        Long userId = userContext.getUserId();
        try{
            purchaseService.purchase(userId);
            redirectAttributes.addFlashAttribute("success", true);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/cart";
    }
}
