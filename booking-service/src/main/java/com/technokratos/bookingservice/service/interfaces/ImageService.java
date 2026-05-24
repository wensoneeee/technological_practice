package com.technokratos.bookingservice.service.interfaces;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Long saveImage(MultipartFile file);

    void writeImageToResponse(Long imageId, HttpServletResponse response);

    void deleteImage(Long id);

    void clearImageIfNotBeingUsed(Long imageId);
}
