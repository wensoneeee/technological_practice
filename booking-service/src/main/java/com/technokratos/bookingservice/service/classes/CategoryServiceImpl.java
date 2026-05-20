package com.technokratos.bookingservice.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.technokratos.bookingservice.dto.dtos.CategoryDto;
import com.technokratos.bookingservice.dto.forms.CategoryForm;
import com.technokratos.bookingservice.models.Category;
import com.technokratos.bookingservice.models.Event;
import com.technokratos.bookingservice.repository.CategoryRepository;
import com.technokratos.bookingservice.repository.EventRepository;
import com.technokratos.bookingservice.service.interfaces.CategoryService;
import com.technokratos.bookingservice.service.interfaces.LoggingService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final LoggingService loggingService;

    @Override
    public List<CategoryDto> getAllCategories(){
        try {
            return categoryRepository.findAll().stream().map(CategoryDto::of).collect(Collectors.toList());
        } catch (Exception e) {
            loggingService.log("ERROR", "getAllCategories", "CategoryServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryDto getCategoryById(Long id){
        try {
            return categoryRepository.findById(id).map(CategoryDto::of).orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException e) {
            loggingService.log("ERROR", "getCategoryById", "CategoryServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(CategoryForm categoryForm){
        try {
            if(categoryForm.id()!=null){
                Category category = categoryRepository.findById(categoryForm.id()).get();
                category.setCategoryDescription(categoryForm.description());
                category.setCategoryName(categoryForm.name());
                categoryRepository.save(category);
            }else{
                Category category = Category.builder()
                        .categoryName(categoryForm.name())
                        .categoryDescription(categoryForm.description())
                        .build();
                categoryRepository.save(category);
            }
        } catch (Exception e) {
            loggingService.log("ERROR", "save", "CategoryServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void addEventCategory(Long eventId, Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            Event event = eventRepository.findById(eventId).orElseThrow();

            category.getEvents().add(event);
            event.getCategories().add(category);

            eventRepository.save(event);
        } catch (Exception e) {
            loggingService.log("ERROR", "addEventCategory", "CategoryServiceImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
