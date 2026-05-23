package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.technokratos.bookingservice.models.CartItem;
import com.technokratos.bookingservice.models.Purchase;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.repository.PurchaseItemRepository;
import com.technokratos.bookingservice.service.interfaces.PurchaseItemService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemServiceImpl implements PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    @Override
    public List<PurchaseItem> transferCartItemToPurchaseItem(List<CartItem> cartItems, Purchase purchase) {
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
    }

    @Override
    public void saveAll(List<PurchaseItem> purchaseItems) {
        purchaseItemRepository.saveAll(purchaseItems);
    }
}
