package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cart_item", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event eventCartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userCartItem;

    private int quantity;
    private BigDecimal pricePerUnit;

    @CreationTimestamp
    private LocalDateTime addedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return cartItemId != null && cartItemId.equals(cartItem.cartItemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return new StringBuilder("CartItem(")
                .append("cartItemId=").append(cartItemId)
                .append(", quantity=").append(quantity)
                .append(", pricePerUnit= ").append(pricePerUnit)
                .append(", addedAt= ").append(addedAt)
                .append(')').toString();
    }
}
