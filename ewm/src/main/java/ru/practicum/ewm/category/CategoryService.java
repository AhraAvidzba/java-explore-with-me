package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    //Методы для админов
    CategoryOutDto addCategory(CategoryRequestDto categoryRequestDto);

    void removeCategory(Long id);

    CategoryOutDto editCategory(CategoryRequestDto categoryRequestDto, Long catId);

    //Публичные методы
    List<CategoryOutDto> getCategories(int from, int size);

    CategoryOutDto getCategoryById(Long catId);
}
