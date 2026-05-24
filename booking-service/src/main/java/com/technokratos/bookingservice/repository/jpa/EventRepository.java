package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategories(Category category);

    boolean existsByImage_ImageId(Long imageId);
}
