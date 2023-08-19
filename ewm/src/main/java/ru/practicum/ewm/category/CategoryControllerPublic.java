package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryOutDto;

import javax.servlet.http.HttpServletRequest;
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
    public List<CategoryOutDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        List<CategoryOutDto> categoryOutDtoList = categoryService.getCategories(from, size);
        log.info("Возвращаются категории. Эндпоинт {}", request.getRequestURI());
        return categoryOutDtoList;
    }

    @GetMapping("/{catId}")
    public CategoryOutDto removeCategory(@PathVariable Long catId,
                                         HttpServletRequest request) {
        log.info("Возвращается категория. Эндпоинт {}", request.getRequestURI());
        return categoryService.getCategoryById(catId);
    }

}
