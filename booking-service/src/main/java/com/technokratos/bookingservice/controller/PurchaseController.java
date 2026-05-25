package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.service.interfaces.PurchaseService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Web Purchase UI", description = "Оформление заказов")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;

    @PostMapping("/purchase")
    @Operation(summary = "Покупка билетов из корзины",
            description = "Оформляет заказ и перенаправляет в корзину с определенным статусом")
    @ApiResponse(responseCode = "302", description = "Заказ обработан. Редирект в корзину")
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
