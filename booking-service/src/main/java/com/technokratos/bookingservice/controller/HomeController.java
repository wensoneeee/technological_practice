package com.technokratos.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.technokratos.bookingservice.dto.dtos.EventDto;
import com.technokratos.bookingservice.service.interfaces.CategoryService;
import com.technokratos.bookingservice.service.interfaces.EventService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Home Controller", description = "Нужен для отображения главной страницы каталога событий")
public class HomeController {

    private final EventService eventService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    @Operation(
            summary = "Отображение главной страницы каталога",
            description = "Генерирует и возвращает страницу (шаблон Freemarker) со списком категорий и фильтрацией событий."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Страница успешно отображается",
                    content = @Content(mediaType = "text/html")
            )
    })
    public String getHomePage(Model model, @RequestParam(required = false) String categoryName) {
        model.addAttribute("categories", categoryService.getAllCategories());

        List<EventDto> events;

        if(categoryName != null) {
            if(categoryName.equals("Все")) {
                events=eventService.findAll();
            }else{
                events = eventService.findByCategory(categoryName);
            }
        }else{
            events=eventService.findAll();
        }
        model.addAttribute("events", events);
        return "home_page";
    }

    @GetMapping("/home/data")
    @ResponseBody
    @Operation(
            summary = "Получение списка событий в формате JSON",
            description = "Возвращает массив всех доступных событий приложения или фильтрует их по названию категории. " +
                    "Используется фронтендом для динамической AJAX-подгрузки элементов без перезагрузки всей страницы."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Массив событий успешно получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class))))
    })
    public List<EventDto> getEventsData(@RequestParam(required = false) String categoryName) {
        if (categoryName != null && !categoryName.equals("Все")) {
            return eventService.findByCategory(categoryName);
        }
        return eventService.findAll();
    }
}
