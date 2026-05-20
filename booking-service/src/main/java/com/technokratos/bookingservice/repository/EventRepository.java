package com.technokratos.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategories(Category category);

    @Query(value = "select e.* from event e " +
                        "left join purchase_item pi on pi.event_id = e.event_id " +
                        "left join purchase p on pi.purchase_id = p.purchase_id and " +
                        "p.purchase_date >= CURRENT_DATE - INTERVAL '7 days' " +
                        "group by e.event_id " +
                        "order by sum(pi.quantity) desc nulls last", nativeQuery = true)
    List<Event> findTopOrderBySalesForLast7Days();

    boolean existsByImage_ImageId(Long imageId);
}
