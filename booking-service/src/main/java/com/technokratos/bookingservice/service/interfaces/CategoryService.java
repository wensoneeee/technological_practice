package com.technokratos.bookingservice.service.interfaces;

import com.technokratos.bookingservice.dto.dtos.CategoryDto;
import com.technokratos.bookingservice.dto.forms.CategoryForm;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Long id);

    void save(CategoryForm categoryForm);

    void addEventCategory(Long eventId, Long categoryId);
}
