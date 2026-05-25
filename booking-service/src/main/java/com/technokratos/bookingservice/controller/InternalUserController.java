package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.models.Feedback;
import com.technokratos.bookingservice.models.Role;
import com.technokratos.bookingservice.models.User;
import com.technokratos.bookingservice.repository.jpa.FeedbackRepository;
import com.technokratos.bookingservice.repository.jpa.UserRepository;
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
public class InternalUserController {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @PostMapping("/api/v1/internal/users")
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
