package com.technokratos.bookingservice.service.interfaces;

import com.technokratos.bookingservice.models.CartItem;
import com.technokratos.bookingservice.models.Purchase;
import com.technokratos.bookingservice.models.PurchaseItem;

import java.util.List;

public interface PurchaseItemService {
    List<PurchaseItem> transferCartItemToPurchaseItem(List<CartItem> cartItems, Purchase purchase);

    void saveAll(List<PurchaseItem> purchaseItems);
}
