package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
