package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.config.RabbitConfig;
import com.technokratos.bookingservice.dto.event.EmailEvent;
import com.technokratos.bookingservice.dto.event.EventActivityEvent;
import com.technokratos.bookingservice.models.User;
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

        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        Purchase purchase = Purchase.builder()
                .userPurchase(user)
                .build();

        purchaseRepository.save(purchase);
        cartItemService.emptyCartForPurchase(userId);

        List<PurchaseItem> purchaseItems = purchaseItemService.transferCartItemToPurchaseItem(cartItemRepository.findCartItemsByUserCartItem_UserId(userId), purchase);
        cartItemService.deleteAllByUserId(userId);
        eventService.updateAvailableTickets(purchaseItems);
        purchaseItemService.saveAll(purchaseItems);

        BigDecimal totalPrice = purchaseItems.stream()
                .map(PurchaseItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        purchase.setTotalPrice(totalPrice);

        for (PurchaseItem purchaseItem : purchaseItems) {
            EventActivityEvent purchaseEvent = new EventActivityEvent(purchaseItem.getEvent().getEventId(), "PURCHASE");
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, purchaseEvent);
        }

        String userEmail = user.getEmail();
        String emailText = String.format("Вы успешно приобрели билеты. Итоговая стоимость заказа: %f руб", totalPrice);

        EmailEvent emailEvent = EmailEvent.builder()
                .toEmail(userEmail)
                .subject("Успешная покупка билетов!")
                .text(emailText)
                .build();

        rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE, RabbitConfig.EMAIL_ROUTING_KEY, emailEvent);

    }
}
