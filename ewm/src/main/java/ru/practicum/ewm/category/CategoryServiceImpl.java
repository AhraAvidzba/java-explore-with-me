package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryOutDto addCategory(CategoryRequestDto categoryRequestDto) {
        Optional<Category> categoryName = categoryRepository.findCategoryByName(categoryRequestDto.getName());
        categoryName.ifPresent(x -> {
            throw new ContentAlreadyExistException("Категортия с именем " + categoryRequestDto.getName() + " уже существует");
        });
        Category category = CategoryMapper.mapToCategory(categoryRequestDto, null);
        return CategoryMapper.mapToCategoryOutDto(categoryRepository.save(category));
    }

    @Override
    public void removeCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new ContentNotFoundException("Категории с id = " + catId + " не существует"));
        List<Event> events = eventRepository.findEventByCategoryId(catId);
        if (!events.isEmpty()) {
            throw new ContentAlreadyExistException("Существуют привязанные к данной категории события, удаление невозможно");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryOutDto editCategory(CategoryRequestDto categoryRequestDto, Long catId) {
        if (catId == null) {
            throw new ContentNotFoundException("Необходимо указать id категории");
        }
        Category sameIdCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new ContentNotFoundException("Категория не найдена"));

        Optional<Category> sameNameCategory = categoryRepository.findCategoryByName(categoryRequestDto.getName());

        if (sameNameCategory.isPresent() && !sameNameCategory.get().getId().equals(sameIdCategory.getId())) {
            throw new ContentAlreadyExistException("Категория с таким именем уже существует");
        }
        Category savedCategory = categoryRepository.save(CategoryMapper.mapToCategory(categoryRequestDto, catId));
        return CategoryMapper.mapToCategoryOutDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryOutDto> getCategories(int from, int size) {
        PageRequest pageable = PageRequest.of(from, size, Sort.by("name").ascending());
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::mapToCategoryOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryOutDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ContentNotFoundException("Категория не найдена"));
        return CategoryMapper.mapToCategoryOutDto(category);
    }
}
