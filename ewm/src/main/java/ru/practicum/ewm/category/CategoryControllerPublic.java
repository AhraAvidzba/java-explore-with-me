package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryInDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
    @RestController
    @RequestMapping(path = "/categories")
    @RequiredArgsConstructor
@Validated
    public class CategoryControllerPublic {
        private final CategoryService categoryService;

        @GetMapping
        public List<CategoryOutDto> getCategory(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
            List<CategoryOutDto> categoryOutDtoList = categoryService.getCategories(from, size);
            log.info("Возвращаются категории");
            return categoryOutDtoList;
        }

        @GetMapping("/{catId}")
        public CategoryOutDto removeCategory(@PathVariable Long catId) {
            log.info("Возвращается категория");
            return categoryService.getCategoryById(catId);
        }

}
