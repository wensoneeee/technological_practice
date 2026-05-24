package com.technokratos.bookingservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.technokratos.bookingservice.models.Image;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageByStorageFileName(String storageFileName);
}
