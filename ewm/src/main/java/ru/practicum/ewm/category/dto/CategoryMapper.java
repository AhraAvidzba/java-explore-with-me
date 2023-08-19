package ru.practicum.ewm.category.dto;

import ru.practicum.ewm.category.Category;

public class CategoryMapper {
    public static Category mapToCategory(CategoryOutDto categoryOutDto) {
        return Category.builder()
                .id(categoryOutDto.getId())
                .name(categoryOutDto.getName())
                .build();
    }

    public static Category mapToCategory(CategoryRequestDto categoryRequestDto, Long catId) {
        return Category.builder()
                .id(catId)
                .name(categoryRequestDto.getName())
                .build();
    }

    public static CategoryOutDto mapToCategoryOutDto(Category category) {
        return CategoryOutDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
