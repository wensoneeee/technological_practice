package com.technokratos.bookingservice.mapper;

import com.technokratos.bookingservice.dto.dtos.CategoryDto;
import com.technokratos.bookingservice.dto.forms.CategoryForm;
import com.technokratos.bookingservice.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "categoryId", target = "id")
    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "categoryDescription", target = "description")
    CategoryDto toDto(Category category);

    @Mapping(source = "id", target = "categoryId")
    @Mapping(source = "name", target = "categoryName")
    @Mapping(source = "description", target = "categoryDescription")
    @Mapping(target = "events", ignore = true)
    Category toEntity(CategoryForm form);

    @Mapping(source = "id", target = "categoryId")
    @Mapping(source = "name", target = "categoryName")
    @Mapping(source = "description", target = "categoryDescription")
    @Mapping(target = "events", ignore = true)
    void updateCategoryFromForm(CategoryForm form, @MappingTarget Category category);
}