package ru.practicum.ewm.category.dto;

import ru.practicum.ewm.category.Category;

public class CategoryMapper {
    public static Category mapToCategory(CategoryOutDto categoryOutDto) {
        return Category.builder()
                .id(categoryOutDto.getId())
                .name(categoryOutDto.getName())
                .build();
    }

    public static Category mapToCategory(CategoryInDto categoryInDto, Long catId) {
        return Category.builder()
                .id(catId)
                .name(categoryInDto.getName())
                .build();
    }

    public static CategoryOutDto mapToCategoryOutDto(Category category) {
        return CategoryOutDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
