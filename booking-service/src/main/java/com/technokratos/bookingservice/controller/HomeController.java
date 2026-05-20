package com.technokratos.bookingservice.controller;

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
public class HomeController {

    private final EventService eventService;
    private final CategoryService categoryService;

    @GetMapping("/home")
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
    public List<EventDto> getEventsData(@RequestParam(required = false) String categoryName) {
        if (categoryName != null && !categoryName.equals("Все")) {
            return eventService.findByCategory(categoryName);
        }
        return eventService.findAll();
    }
}
