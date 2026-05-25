package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.dto.dtos.UserDto;
import com.technokratos.bookingservice.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.service.classes.UserServiceImpl;
import com.technokratos.bookingservice.service.interfaces.ImageService;
import com.technokratos.bookingservice.validation.ImageValidator;
import com.technokratos.bookingservice.validation.Validation;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Tag(name = "Image Controller", description = "Загрузка, отображение и удаление изображения")
public class ImageController {

    private final ImageService imageService;
    private final UserServiceImpl userService;
    private final ImageValidator imageValidator;

    @GetMapping("/image/{imageId}")
    @Operation(summary = "Получить изображение", description = "Записывает байты картинки напрямую в ответ")
    @ApiResponse(responseCode = "200", description = "Файл изображения",
            content = @Content(mediaType = "image/jpeg"))
    public void getImage(@PathVariable Long imageId, HttpServletResponse response) {
        imageService.writeImageToResponse(imageId, response);
    }

    @PostMapping("/img/profile/update")
    @Operation(summary = "Обновить аватар профиля")
    @ApiResponse(responseCode = "302", description = "Редирект в профиль")
    public String saveProfileImage(@RequestParam("file") MultipartFile file,
                                   @AuthenticationPrincipal String email, RedirectAttributes redirectAttributes) {
        Validation validation = imageValidator.validate(file);

        if(validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else {
            userService.changeProfileImage(imageService.saveImage(file), email);
        }
        return "redirect:/profile";
    }

    @PostMapping("/img/profile/delete")
    @Operation(summary = "Удалить аватар профиля")
    @ApiResponse(responseCode = "302", description = "Редирект в профиль")
    public String deleteProfileImage(@AuthenticationPrincipal String email) {
        userService.deleteProfileImage(email);
        return "redirect:/profile";
    }
}
