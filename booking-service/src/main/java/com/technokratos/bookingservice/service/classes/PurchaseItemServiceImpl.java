package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.models.CartItem;
import com.technokratos.bookingservice.models.Purchase;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.repository.PurchaseItemRepository;
import com.technokratos.bookingservice.service.interfaces.LoggingService;
import com.technokratos.bookingservice.service.interfaces.PurchaseItemService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemServiceImpl implements PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final LoggingService loggingService;

    @Override
    public List<PurchaseItem> transferCartItemToPurchaseItem(List<CartItem> cartItems, Purchase purchase) {
        try {
            List<PurchaseItem> purchaseItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                PurchaseItem purchaseItem = PurchaseItem.builder()
                            .purchase(purchase)
                            .event(cartItem.getEventCartItem())
                            .pricePerUnit(cartItem.getPricePerUnit())
                            .quantity(cartItem.getQuantity())
                            .subTotal(cartItem.getPricePerUnit().multiply(new BigDecimal(cartItem.getQuantity())))
                            .build();
                purchaseItems.add(purchaseItem);
            }
            return purchaseItems;
        } catch (Exception e) {
            loggingService.log("ERROR", "transferCartItemToPurchaseItem", "PurchaseItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<PurchaseItem> purchaseItems){
        try {
            purchaseItemRepository.saveAll(purchaseItems);
        } catch (Exception e) {
            loggingService.log("ERROR", "saveAll", "PurchaseItemServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
