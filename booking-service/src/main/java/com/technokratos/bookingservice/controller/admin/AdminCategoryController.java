package com.technokratos.bookingservice.controller.admin;

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
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CategoryValidator categoryValidator;

    @GetMapping("/admin/category")
    public String getAdminCategory(Model model, @RequestParam(required = false) Long selectedCategoryId) {
        if (selectedCategoryId != null){
            model.addAttribute("selectedCategory", categoryService.getCategoryById(selectedCategoryId));
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("events", eventService.findAll());
        return "admin/admin_category_page";
    }

    @PostMapping("/admin/category")
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
    public String postAddEvent(@RequestParam Long selectedEventId, @RequestParam Long selectedCategoryId) {
        categoryService.addEventCategory(selectedEventId, selectedCategoryId);
        return "redirect:/admin/category";
    }
}
