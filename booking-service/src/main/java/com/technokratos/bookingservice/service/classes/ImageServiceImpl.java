package com.technokratos.bookingservice.service.classes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.technokratos.bookingservice.models.Image;
import com.technokratos.bookingservice.repository.jpa.EventRepository;
import com.technokratos.bookingservice.repository.jpa.ImageRepository;
import com.technokratos.bookingservice.service.interfaces.ImageService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;

    @Value("${storage.path}")
    private String storagePath;

    @Override
    public Long saveImage(MultipartFile file) {
        String storageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Image image = Image.builder()
                .originalFileName(file.getOriginalFilename())
                .storageFileName(storageName)
                .type(file.getContentType())
                .size(file.getSize())
                .build();

        try {
            Files.copy(file.getInputStream(), Path.of(storagePath, storageName));
        } catch (IOException e) {
            throw new RuntimeException("не удалось сохранить файл");
        }
        imageRepository.save(image);
        return imageRepository.findImageByStorageFileName(storageName).getImageId();
    }

    @Override
    public void writeImageToResponse(Long imageId, HttpServletResponse response) {
        Image image = imageRepository.findById(imageId).get();
        response.setContentType(image.getType());

        try {
            IOUtils.copy(new FileInputStream(Path.of(storagePath, image.getStorageFileName()).toString()), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).get();
        try {
            Files.delete(Path.of(storagePath, image.getStorageFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageRepository.deleteById(id);
    }

    @Override
    public void clearImageIfNotBeingUsed(Long imageId) {
        if (imageId.equals(1L)) return;

        if (!eventRepository.existsByImage_ImageId(imageId)) {
            deleteImage(imageId);
        }
    }
}
