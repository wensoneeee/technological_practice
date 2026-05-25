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
import com.technokratos.bookingservice.dto.forms.CategoryForm;
import com.technokratos.bookingservice.service.interfaces.CategoryService;
import com.technokratos.bookingservice.service.interfaces.EventService;
import com.technokratos.bookingservice.validation.CategoryValidator;
import com.technokratos.bookingservice.validation.Validation;

@Controller
@RequiredArgsConstructor
@Tag(name = "Admin Category UI", description = "Панель администратора: Управление категориями и привязкой событий")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CategoryValidator categoryValidator;

    @GetMapping("/admin/category")
    @Operation(summary = "Страница управления категориями", description = "Отображает список всех категорий и форму редактирования")
    @ApiResponse(responseCode = "200", description = "Cтраница успешно загружена",
            content = @Content(mediaType = "text/html"))
    public String getAdminCategory(Model model, @RequestParam(required = false) Long selectedCategoryId) {
        if (selectedCategoryId != null){
            model.addAttribute("selectedCategory", categoryService.getCategoryById(selectedCategoryId));
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("events", eventService.findAll());
        return "admin/admin_category_page";
    }

    @PostMapping("/admin/category")
    @Operation(summary = "Создание или обновление категории",
            description = "Принимает форму категории, валидирует её и делает редирект")
    @ApiResponse(responseCode = "302", description = "Категория сохранена или ошибка валидации. Редирект")
    public String postAdminCategory(RedirectAttributes redirectAttributes, @ModelAttribute CategoryForm categoryForm) {
        Validation validation = categoryValidator.validate(categoryForm);

        if(validation.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
        }else {
            categoryService.save(categoryForm);
        }
        return "redirect:/admin/category";
    }

    @PostMapping("/admin/category/addEvent")
    @Operation(summary = "Привязка события к категории", description = "Связывает выбранное событие с категорией")
    @ApiResponse(responseCode = "302", description = "Событие добавлено к категории успешно. Редирект")
    public String postAddEvent(@RequestParam Long selectedEventId, @RequestParam Long selectedCategoryId) {
        categoryService.addEventCategory(selectedEventId, selectedCategoryId);
        return "redirect:/admin/category";
    }
}
