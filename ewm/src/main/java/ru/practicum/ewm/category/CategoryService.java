package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryInDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;

import java.util.List;

public interface CategoryService {
    //Методы для админов
    CategoryOutDto addCategory(CategoryInDto categoryInDto);

    void removeCategory(Long id);

    CategoryOutDto editCategory(CategoryInDto categoryInDto, Long catId);

    //Публичные методы
    List<CategoryOutDto> getCategories(int from, int size);

    CategoryOutDto getCategoryById(Long catId);
}
