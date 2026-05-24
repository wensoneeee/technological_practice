package com.technokratos.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.CartItemForm;
import com.technokratos.bookingservice.service.interfaces.CartItemService;
import com.technokratos.bookingservice.service.interfaces.UserService;
import com.technokratos.bookingservice.validation.CartItemValidator;
import com.technokratos.bookingservice.validation.Validation;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final UserService userService;
    private final CartItemService cartItemService;
    private final CartItemValidator cartItemValidator;

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute CartItemForm cartItemForm, Principal principal, RedirectAttributes redirectAttributes) {
        cartItemForm.setUserId(userService.getUserByEmail(principal.getName()).getId());
        Validation validation = cartItemValidator.validate(cartItemForm);

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else {
            cartItemService.save(cartItemForm);
        }
        return "redirect:/event/"+cartItemForm.getEventId();
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        BigDecimal totalPrice = cartItemService.getTotalPrice(userId);
        if(totalPrice!=null) {
            model.addAttribute("cartItems", cartItemService.getByUserId(userId));
            model.addAttribute("totalPriceRub", totalPrice);
        }
        return "cart_page";
    }
}
