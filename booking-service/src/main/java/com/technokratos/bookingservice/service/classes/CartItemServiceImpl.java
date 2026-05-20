package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.dto.dtos.CartItemDto;
import com.technokratos.bookingservice.dto.forms.CartItemForm;
import com.technokratos.bookingservice.models.CartItem;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.CartItemRepository;
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.repository.UserRepository;
import com.technokratos.bookingservice.service.interfaces.CartItemService;
import com.technokratos.bookingservice.service.interfaces.LoggingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LoggingService loggingService;

    @Override
    public CartItem save(CartItemForm cartItemForm){
        try {
            User user = userRepository.findById(cartItemForm.getUserId()).orElseThrow(IllegalArgumentException::new);
            Event event = eventRepository.findById(cartItemForm.getEventId()).orElseThrow(IllegalArgumentException::new);

            CartItem cartItem;
            if (cartItemRepository.existsCartItemByUserCartItemAndEventCartItem(user, event)){
                cartItem = cartItemRepository.findCartItemByUserCartItemAndEventCartItem(user, event);
                cartItem.setAddedAt(LocalDateTime.now());
                cartItem.setQuantity(cartItemForm.getQuantity());
                cartItem.setPricePerUnit(cartItemForm.getPrice());
            }else {
                cartItem = CartItem.builder()
                        .userCartItem(user)
                        .eventCartItem(event)
                        .pricePerUnit(cartItemForm.getPrice())
                        .quantity(cartItemForm.getQuantity())
                        .build();
            }

            return cartItemRepository.save(cartItem);
        } catch (IllegalArgumentException e) {
            loggingService.log("ERROR", "save", "CartItemServiceImpl", "save выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CartItemDto> getByUserId(Long userId){
        try {
            return cartItemRepository.findCartItemsByUserCartItem_UserId(userId).stream().map(cartItem -> {
                return CartItemDto.of(cartItem, cartItem.getEventCartItem().getTitle());
            }).collect(Collectors.toList());
        } catch (Exception e) {
            loggingService.log("ERROR", "getByUserId", "CartItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal getTotalPrice(Long userId){
        try {
            return cartItemRepository.getTotalPriceByUserId(userId);
        } catch (Exception e) {
            loggingService.log("ERROR", "getTotalPrice", "CartItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void emptyCartForPurchase(Long userId){
        try {
            List<CartItem> cartItems = cartItemRepository.findCartItemsByUserCartItem_UserId(userId);
            boolean hasEveryItem = true;
            for (CartItem cartItem : cartItems) {
                if (!(cartItem.getQuantity()<=cartItem.getEventCartItem().getAvailableTickets())){
                    cartItemRepository.delete(cartItem);
                    hasEveryItem = false;
                }
            }
            if (!hasEveryItem){
                throw new RuntimeException("не все мероприятия есть в наличии");
            }
        } catch (RuntimeException e) {
            loggingService.log("ERROR", "emptyCartForPurchase", "CartItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllByUserId(Long userId){
        try {
            cartItemRepository.deleteAllByUserCartItem_UserId(userId);
        } catch (Exception e) {
            loggingService.log("ERROR", "deleteAllByUserId", "CartItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
