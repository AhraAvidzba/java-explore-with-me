package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryOutDto addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryOutDto categoryOutDto = categoryService.addCategory(categoryRequestDto);
        log.info("Категория сохранена, id = {}", categoryOutDto.getId());
        return categoryOutDto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long catId) {
        categoryService.removeCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryOutDto editCategory(@PathVariable Long catId,
                                       @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.editCategory(categoryRequestDto, catId);
    }
}
