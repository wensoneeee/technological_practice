package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseItem purchaseItem = (PurchaseItem) o;
        return purchaseItemId != null && purchaseItemId.equals(purchaseItem.purchaseItemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("PurchaseItem(")
                .append("purchaseItemId=").append(purchaseItemId)
                .append(", quantity=").append(quantity)
                .append(", pricePerUnit=").append(pricePerUnit)
                .append(", subTotal=").append(subTotal)
                .append(')').toString();
    }
}
