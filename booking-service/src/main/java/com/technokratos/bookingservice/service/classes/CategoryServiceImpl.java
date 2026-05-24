package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.technokratos.bookingservice.dto.dtos.CategoryDto;
import com.technokratos.bookingservice.dto.forms.CategoryForm;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.repository.CategoryRepository;
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.service.interfaces.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void save(CategoryForm categoryForm) {
        if (categoryForm.id() != null) {
            Category category = categoryRepository.findById(categoryForm.id()).get();
            categoryMapper.updateCategoryFromForm(categoryForm, category);
            categoryRepository.save(category);
        } else {
            Category category = categoryMapper.toEntity(categoryForm);
            categoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void addEventCategory(Long eventId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();

        category.getEvents().add(event);
        event.getCategories().add(category);

        eventRepository.save(event);
    }
}
