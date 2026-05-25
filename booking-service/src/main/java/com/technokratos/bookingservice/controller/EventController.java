package com.technokratos.bookingservice.controller;

import com.technokratos.bookingservice.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.FeedbackService;
import com.technokratos.bookingservice.validation.FeedbackValidator;
import com.technokratos.bookingservice.validation.Validation;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Web Event UI", description = "Отображение страницы события и отправка отзыва")
public class EventController {
    private final EventService eventService;
    private final FeedbackService feedbackService;
    private final FeedbackValidator feedbackValidator;
    private final UserService userService;

    @GetMapping("/event/{event-id}")
    @Operation(summary = "Страница события по ID", description = "Возвращает страницу с деталями события и отзывами")
    @ApiResponse(responseCode = "200", description = "страница события",
            content = @Content(mediaType = "text/html"))
    public String getEvent(@PathVariable("event-id") Long eventId, Model model) {
        try {
            model.addAttribute("event", eventService.findById(eventId));
            model.addAttribute("avgScore", feedbackService.getAverageEventScore(eventId));
            model.addAttribute("feedbacks", feedbackService.findCommentsByEventId(eventId));
        }catch (IllegalArgumentException e){
            model.addAttribute("error", true);
        }
        return "event_page";
    }

    @PostMapping("/event/{event-id}")
    @Operation(summary = "Отправка отзыва к событию", description = "Сохраняет отзыв и перенаправляет обратно")
    @ApiResponse(responseCode = "302", description = "Редирект на страницу события")
    public String sendComment(@PathVariable("event-id") Long eventId, @ModelAttribute FeedbackForm feedbackForm, Principal principal, RedirectAttributes redirectAttributes) {
        feedbackForm.setUserId(userService.getUserByEmail(principal.getName()).getId());
        feedbackForm.setEventId(eventId);
        Validation validation = feedbackValidator.validate(feedbackForm);

        if(validation.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else{
            feedbackService.save(feedbackForm);
        }
        return "redirect:/event/" + eventId;
    }
}
