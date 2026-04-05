package com.technokratos.bookingservice.repository;

import com.technokratos.bookingservice.entity.ListingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingDetailsRepository extends JpaRepository<ListingDetails, Long> {
}
