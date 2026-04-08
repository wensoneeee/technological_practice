package com.technokratos.bookingservice.repository;

import com.technokratos.bookingservice.entity.ListingPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingPhotoRepository extends JpaRepository<ListingPhoto, Long> {

}
