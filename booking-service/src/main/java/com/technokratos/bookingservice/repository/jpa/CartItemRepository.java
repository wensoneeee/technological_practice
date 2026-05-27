package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.CartItem;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemsByUserCartItem_UserId(Long userId);
    @Query("select sum(pricePerUnit*quantity)" +
            "from CartItem where userCartItem.userId=:userId")
    BigDecimal getTotalPriceByUserId(Long userId);

    void deleteAllByUserCartItem_UserId(Long userCartItemUserId);

    boolean existsCartItemByUserCartItemAndEventCartItem(User userCartItem, Event eventCartItem);

    CartItem findCartItemByUserCartItemAndEventCartItem(User user, Event event);

    void deleteCartItemByEventCartItem_EventIdAndUserCartItem_UserId(Long eventCartItemEventId, Long userCartItemUserId);
}
