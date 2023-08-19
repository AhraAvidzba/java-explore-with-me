package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryOutDto addCategory(@RequestBody CategoryRequestDto categoryRequestDto,
                                      HttpServletRequest request) {
        CategoryOutDto categoryOutDto = categoryService.addCategory(categoryRequestDto);
        log.info("Категория сохранена. Эндпоинт {}", request.getRequestURI());
        return categoryOutDto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long catId,
                               HttpServletRequest request) {
        categoryService.removeCategory(catId);
        log.info("Категория удалена. Эндпоинт {}", request.getRequestURI());
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryOutDto editCategory(@PathVariable Long catId,
                                       @RequestBody CategoryRequestDto categoryRequestDto,
                                       HttpServletRequest request) {
        log.info("Категория изменена. Эндпоинт {}", request.getRequestURI());
        return categoryService.editCategory(categoryRequestDto, catId);
    }
}
