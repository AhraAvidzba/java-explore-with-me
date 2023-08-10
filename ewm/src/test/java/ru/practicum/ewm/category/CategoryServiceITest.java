package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm.category.dto.CategoryInDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFountException;
import ru.practicum.ewm.user.dto.ShortUserDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:ewm",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles(profiles = "test")
@Transactional
class CategoryServiceITest {
    private final CategoryService categoryService;

    @Test
    void editCategory() {
        //given
        List<CategoryInDto> sourceCategories = List.of(
                makeCategoryInDto("Концерты"),
                makeCategoryInDto("Кино"),
                makeCategoryInDto("Еда")
        );
        sourceCategories.forEach(categoryService::addCategory);
        //when
        CategoryInDto categoryOutDto = makeCategoryInDto("Спорт");
        CategoryOutDto targetCategory = categoryService.editCategory(categoryOutDto, 1L);
        //then
        assertThat(targetCategory.getName(), equalTo("Спорт"));
    }

    //@Rollback(true)
    @Test
    void editCategory_whenUpdateSameName_thenContentAlreadyExistExceptionThrown() {
        //given
        List<CategoryInDto> sourceCategories = List.of(
                makeCategoryInDto("Концерты"),
                makeCategoryInDto("Кино"),
                makeCategoryInDto("Еда")
        );

        List<CategoryOutDto> savedCategories  = new ArrayList<>();
        sourceCategories.forEach(x -> {
                    CategoryOutDto categoryInDto = categoryService.addCategory(x);
                    savedCategories.add(categoryInDto);
                });
        Long savedId = savedCategories.get(0).getId();
        List<CategoryOutDto> categories2 = categoryService.getCategories(0, 10);
        CategoryInDto categoryInDto = makeCategoryInDto("Кино");
        //when...then
        Assertions.assertThrows(
                ContentAlreadyExistException.class,
                () -> categoryService.editCategory(categoryInDto, savedId));
    }

    @Test
    void editCategory_whenUpdateUncreatedId_thenContentNotFountExceptionThrown() {
        //given
        CategoryInDto categoryInDto = makeCategoryInDto("Кино");
        //when...then
        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> categoryService.editCategory(categoryInDto, 1L));
    }


    @Test
    void getCategories() {
        //given
        List<CategoryInDto> sourceCategories = List.of(
                makeCategoryInDto("Концерты"),
                makeCategoryInDto("Кино"),
                makeCategoryInDto("Еда")
        );

        List<CategoryOutDto> savedCategories = new ArrayList<>();
        sourceCategories.forEach(x -> {
            CategoryOutDto categoryInDto = categoryService.addCategory(x);
            savedCategories.add(categoryInDto);
        });

        //when
        List<CategoryOutDto> returnedCategories = categoryService.getCategories(0, 10);
        //then
        for (CategoryOutDto categoryOutDto : savedCategories) {
            assertThat(returnedCategories, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(categoryOutDto.getName()))
            )));
        }
    }

    @Test
    void removeCategory() {
        //given
        CategoryInDto sourceCategory = makeCategoryInDto("Концерты");

        Long savedId = categoryService.addCategory(sourceCategory).getId();
        //when
        categoryService.removeCategory(savedId);
        //then
        List<CategoryOutDto> returnedCategories = categoryService.getCategories(0, 10);
        assertThat(returnedCategories.size(), equalTo(0));
    }

    @Test
    void getCategoryById() {
        //given
        CategoryInDto sourceCategory = makeCategoryInDto("Концерты");

        Long savedId = categoryService.addCategory(sourceCategory).getId();

        //when
        CategoryOutDto categoryOutDto = categoryService.getCategoryById(savedId);
        //then
        assertThat(categoryOutDto.getName(), equalTo("Концерты"));
    }


    private CategoryInDto makeCategoryInDto(String name) {
        return CategoryInDto.builder()
        .name(name)
        .build();
    }
}