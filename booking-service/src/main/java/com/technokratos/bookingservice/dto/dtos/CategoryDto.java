package com.technokratos.bookingservice.dto.dtos;

import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.Category;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CategoryDto {
    private String name;
    private String description;
    private Long id;

    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
                .name(category.getCategoryName())
                .description(category.getCategoryDescription())
                .id(category.getCategoryId())
                .build();
    }

    public static List<CategoryDto> from(List<Category> categories) {
        return categories.stream().map(CategoryDto::of).collect(Collectors.toList());
    }
}
