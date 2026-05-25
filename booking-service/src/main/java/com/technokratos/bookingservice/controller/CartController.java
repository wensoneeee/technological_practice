package com.technokratos.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Web Cart UI", description = "Управление корзиной покупок")
public class CartController {

    private final UserService userService;
    private final CartItemService cartItemService;
    private final CartItemValidator cartItemValidator;

    @PostMapping("/cart/add")
    @Operation(summary = "Добавление билета в корзину",
            description = "Принимает форму добавления и делает редирект на страницу события")
    @ApiResponse(responseCode = "302", description = "Успешное добавление/ошибка валидации. Редирект")
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
    @Operation(summary = "Просмотр корзины")
    @ApiResponse(responseCode = "200", description = "HTML-страница корзины",
            content = @Content(mediaType = "text/html"))
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
