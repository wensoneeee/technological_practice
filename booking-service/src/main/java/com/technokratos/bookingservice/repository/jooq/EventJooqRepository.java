package com.technokratos.bookingservice.repository.jooq;

import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.models.Image;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.technokratos.bookingservice.jooq.gen.Tables.EVENT;
import static com.technokratos.bookingservice.jooq.gen.Tables.PURCHASE;
import static com.technokratos.bookingservice.jooq.gen.Tables.PURCHASE_ITEM;

@Repository
@RequiredArgsConstructor
public class EventJooqRepository {

    private final DSLContext dsl;

    public List<Event> findTopOrderBySalesForLast7Days() {
        return dsl.select(
                        EVENT.EVENT_ID,
                        EVENT.TITLE,
                        EVENT.DESCRIPTION,
                        EVENT.TIMESTAMP,
                        EVENT.LOCATION,
                        EVENT.PRICE,
                        EVENT.AVAILABLE_TICKETS,
                        EVENT.IMAGE_ID
                )
                .from(EVENT)
                .leftJoin(PURCHASE_ITEM).on(PURCHASE_ITEM.EVENT_ID.eq(EVENT.EVENT_ID))
                .leftJoin(PURCHASE).on(PURCHASE.PURCHASE_ID.eq(PURCHASE_ITEM.PURCHASE_ID)
                        .and(PURCHASE.PURCHASE_DATE.ge(LocalDate.from(LocalDateTime.now().minusDays(7)))))
                .groupBy(EVENT.EVENT_ID)
                .orderBy(DSL.sum(PURCHASE_ITEM.QUANTITY).desc().nullsLast())
                .fetch(record -> {
                    Image image = null;
                    Long imageId = record.get(EVENT.IMAGE_ID);
                    if (imageId != null) {
                        image = Image.builder().imageId(imageId).build();
                    }

                    return Event.builder()
                            .eventId(record.get(EVENT.EVENT_ID))
                            .title(record.get(EVENT.TITLE))
                            .description(record.get(EVENT.DESCRIPTION))
                            .timestamp(record.get(EVENT.TIMESTAMP))
                            .location(record.get(EVENT.LOCATION))
                            .price(record.get(EVENT.PRICE))
                            .availableTickets(record.get(EVENT.AVAILABLE_TICKETS))
                            .image(image)
                            .build();
                });
    }
}