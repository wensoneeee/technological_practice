package com.technokratos.bookingservice.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.dtos.FeedbackDto;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;
import com.technokratos.bookingservice.service.interfaces.FeedbackService;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.UserService;
import com.technokratos.bookingservice.validation.FeedbackValidator;
import com.technokratos.bookingservice.validation.Validation;

@Controller
@RequiredArgsConstructor
@Tag(name = "Admin Feedback UI", description = "Панель админа: Управление отзывами пользователей")
public class AdminFeedbackController {
    private final UserService userService;
    private final EventService eventService;
    private final FeedbackService feedbackService;
    private final FeedbackValidator feedbackValidator;

    @GetMapping("/admin/feedback")
    @Operation(summary = "Страница управления отзывами",
            description = "Отображает списки пользователей, событий и карточку конкретного отзыва")
    @ApiResponse(responseCode = "200", description = "Cтраница спешно загружена",
            content = @Content(mediaType = "text/html"))
    public String adminFeedback(Model model, @RequestParam(required = false) Long selectedEventId, @RequestParam(required = false) Long selectedUserId) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("events", eventService.findAll());

        if (selectedUserId != null && selectedEventId != null) {
            FeedbackDto selectedFeedback = feedbackService.getFeedbackByUserIdAndEventId(selectedUserId, selectedEventId);
            if (selectedFeedback == null) {
                selectedFeedback = FeedbackDto.builder().userId(selectedUserId).eventId(selectedEventId).build();
            }
            model.addAttribute("selectedFeedback", selectedFeedback);
        }
        return "admin/admin_feedback_page";
    }

    @PostMapping("/admin/feedback/save")
    @Operation(summary = "Сохранение или изменение отзыва",
            description = "Можно создать или отредактировать текст/оценку отзыва")
    @ApiResponse(responseCode = "302", description = "Отзыв успешно обновлен. Редирект")
    public String saveFeedback(@ModelAttribute FeedbackForm feedbackForm, RedirectAttributes redirectAttributes){
        Validation validation = feedbackValidator.validate(feedbackForm);

        if(validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else {
            feedbackService.save(feedbackForm);
        }
        return "redirect:/admin/feedback?selectedEventId=" + feedbackForm.getEventId() + "&selectedUserId=" + feedbackForm.getUserId();
    }
}
