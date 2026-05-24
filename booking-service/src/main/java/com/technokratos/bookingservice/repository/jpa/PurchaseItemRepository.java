package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.PurchaseItem;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
