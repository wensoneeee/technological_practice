package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.config.RabbitConfig;
import com.technokratos.bookingservice.dto.event.EventActivityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.technokratos.bookingservice.models.Purchase;
import com.technokratos.bookingservice.models.PurchaseItem;
import com.technokratos.bookingservice.repository.jpa.CartItemRepository;
import com.technokratos.bookingservice.repository.jpa.PurchaseRepository;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
import com.technokratos.bookingservice.service.interfaces.CartItemService;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.FeedbackService;
import com.technokratos.bookingservice.service.interfaces.PurchaseItemService;
import com.technokratos.bookingservice.service.interfaces.PurchaseService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;
    private final PurchaseItemService purchaseItemService;
    private final EventService eventService;
    private final FeedbackService feedbackService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void purchase(Long userId){

        Purchase purchase = Purchase.builder()
                .userPurchase(userRepository.findById(userId).orElseThrow(IllegalArgumentException::new))
                .build();

        purchaseRepository.save(purchase);
        cartItemService.emptyCartForPurchase(userId);
        List<PurchaseItem> purchaseItems = purchaseItemService.transferCartItemToPurchaseItem(cartItemRepository.findCartItemsByUserCartItem_UserId(userId), purchase);
        cartItemService.deleteAllByUserId(userId);
        eventService.updateAvailableTickets(purchaseItems);
        purchaseItemService.saveAll(purchaseItems);
        purchase.setTotalPrice(
                purchaseItems.stream().map(PurchaseItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        for (PurchaseItem purchaseItem : purchaseItems) {
            EventActivityEvent purchaseEvent = new EventActivityEvent(purchaseItem.getEvent().getEventId(), "PURCHASE");
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, purchaseEvent);
        }


    }
}
