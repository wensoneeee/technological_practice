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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.technokratos.bookingservice.dto.forms.EventForm;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.service.interfaces.ImageService;
import com.technokratos.bookingservice.validation.EventValidator;
import com.technokratos.bookingservice.validation.Validation;

@Controller
@RequiredArgsConstructor
@Tag(name = "Admin Event UI",
        description = "Панель админа: Управление каталогом событий")
public class AdminEventController {

    private final EventService eventService;
    private final ImageService imageService;
    private final EventValidator eventValidator;

    @GetMapping("/admin/event")
    @Operation(summary = "Страница управления событиями",
            description = "Отображает список мероприятий и их детали")
    @ApiResponse(responseCode = "200", description = "Страница успешно загружена",
            content = @Content(mediaType = "text/html"))
    public String getAdminEvent(@RequestParam(required = false) Long selectedEventId, Model model) {
        model.addAttribute("events", eventService.findAll());

        if (selectedEventId != null) {
            model.addAttribute("selectedEvent", eventService.findById(selectedEventId));
        }
        return "admin/admin_event_page";
    }

    @PostMapping("/admin/event/save")
    @Operation(summary = "Создание или обновление события",
            description = "Сохраняет детали события и его изображение (опционально)")
    @ApiResponse(responseCode = "302", description = "Событие сохранено или произошла ошибка. Редирект")
    public String createEvent(@ModelAttribute EventForm eventForm, @RequestParam(required = false) MultipartFile file, RedirectAttributes redirectAttributes) {

        Validation validation = eventValidator.validate(eventForm);

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/admin/event";
        }

        try {
            if (file != null && !file.isEmpty()) {
                Long imageId = imageService.saveImage(file);
                Long eventId = eventService.save(eventForm).getId();
                eventService.changeEventPhoto(imageId, eventId);
            } else {
                eventService.save(eventForm);
            }
            return "redirect:/admin/event";
        } catch (Exception e) {
            return "redirect:/admin/event?error";
        }
    }

    @PostMapping("/admin/event/delete")
    @Operation(summary = "Удаление события по ID", description = "Полностью удаляет мероприятие из системы")
    @ApiResponse(responseCode = "302", description = "Событие удалено. Редирект")
    public String deleteEvent(@RequestParam Long id) {
        eventService.delete(id);
        return "redirect:/admin/event";
    }
}
