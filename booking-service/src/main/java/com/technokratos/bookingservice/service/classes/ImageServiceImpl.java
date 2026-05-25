package com.technokratos.bookingservice.service.classes;

import io.minio.*;
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

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

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
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storageName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить файл в MinIO", e);
        }

        imageRepository.save(image);
        return imageRepository.findImageByStorageFileName(storageName).getImageId();
    }

    @Override
    public void writeImageToResponse(Long imageId, HttpServletResponse response) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Изображение не найдено"));

        response.setContentType(image.getType());

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(image.getStorageFileName())
                        .build())) {

            IOUtils.copy(stream, response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении файла из MinIO", e);
        }
    }

    @Override
    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Изображение не найдено"));
        try {
            // Удаляем файл из MinIO
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(image.getStorageFileName())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла из MinIO", e);
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