package com.technokratos.bookingservice.service.classes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.technokratos.bookingservice.models.Image;
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.repository.ImageRepository;
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

    private final LoggingService loggingService;

    @Override
    public Long saveImage(MultipartFile file){
        try {
            String storageName = UUID.randomUUID()+"_"+file.getOriginalFilename();
            Image image = Image.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storageFileName(storageName)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .build();

            try{
                Files.copy(file.getInputStream(), Path.of(storagePath, storageName));
            }catch (IOException e){
                throw new RuntimeException("не удалось сохранить файл");
            }
            imageRepository.save(image);
            return imageRepository.findImageByStorageFileName(storageName).getImageId();
        } catch (RuntimeException e) {
            loggingService.log("ERROR", "saveImage", "ImageServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeImageToResponse(Long imageId, HttpServletResponse response){
        try {
            Image image = imageRepository.findById(imageId).get();
            response.setContentType(image.getType());

            try{
                IOUtils.copy(new FileInputStream(Path.of(storagePath, image.getStorageFileName()).toString()), response.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            loggingService.log("ERROR", "writeImageToResponse", "ImageServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(Long id){
        try {
            Image image = imageRepository.findById(id).get();
            Files.delete(Path.of(storagePath, image.getStorageFileName()));
            imageRepository.deleteById(id);
        } catch (Exception e) {
            loggingService.log("ERROR", "deleteImage", "ImageServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clearImageIfNotBeingUsed(Long imageId){
        try {
            if (imageId.equals(1L)) return;

            if (!eventRepository.existsByImage_ImageId(imageId)) {
                deleteImage(imageId);
            }
        } catch (Exception e) {
            loggingService.log("ERROR", "clearImageIfNotBeingUsed", "ImageServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
