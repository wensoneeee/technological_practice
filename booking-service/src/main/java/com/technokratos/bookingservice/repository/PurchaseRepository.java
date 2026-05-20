package com.technokratos.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {


    @Query("select case when count(pi)>0 then true else false end " +
            "from PurchaseItem pi " +
            "join pi.purchase p " +
            "where pi.event.eventId=:eventId and p.userPurchase.userId = :userId")
    boolean didUserBoughtTicket(Long eventId, Long userId);
}
