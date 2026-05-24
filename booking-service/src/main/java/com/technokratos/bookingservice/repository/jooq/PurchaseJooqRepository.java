package com.technokratos.bookingservice.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.technokratos.bookingservice.jooq.gen.Tables.PURCHASE;
import static com.technokratos.bookingservice.jooq.gen.Tables.PURCHASE_ITEM;

@Repository
@RequiredArgsConstructor
public class PurchaseJooqRepository {

    private final DSLContext dsl;

    public boolean didUserBoughtTicket(Long eventId, Long userId) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(PURCHASE_ITEM)
                        .join(PURCHASE).on(PURCHASE_ITEM.PURCHASE_ID.eq(PURCHASE.PURCHASE_ID))
                        .where(PURCHASE_ITEM.EVENT_ID.eq(eventId))
                        .and(PURCHASE.USER_ID.eq(userId)) // Убедись, что в PURCHASE есть колонка USER_ID
        );
    }
}