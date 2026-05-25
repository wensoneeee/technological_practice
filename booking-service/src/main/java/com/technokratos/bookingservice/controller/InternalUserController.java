package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.models.Feedback;
import com.technokratos.bookingservice.models.Role;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.jpa.FeedbackRepository;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Internal User Controller", description = "Внутренний API для синхронного межсервисного взаимодействия (вызовы из auth-service и планировщиков)")
public class InternalUserController {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @PostMapping("/api/v1/internal/users")
    @Operation(
            summary = "Создание локального профиля пользователя",
            description = "Вызывается сервисом аутентификации сразу после успешной регистрации для синхронизации данных пользователя в базе Booking Service."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно продублирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации переданных параметров запроса")
    })
    public ResponseEntity<Void> createProfile(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("role") Role role){
        User user = User.builder()
                .email(email)
                .name(name)
                .role(role)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/internal/feedbacks/{feedbackId}/status")
    @Operation(
            summary = "Изменение статуса модерации отзыва",
            description = "Внутренний эндпоинт для обновления статуса отзыва (например, после автоматической проверки модератором или сторонним сервисом)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус отзыва успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Отзыв с указанным ID не найден в системе")
    })
    @Transactional
    public ResponseEntity<Void> updateFeedbackStatus(@PathVariable Long feedbackId, @RequestParam String status) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback != null) {
            feedback.setStatus(status);
            feedbackRepository.save(feedback);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
