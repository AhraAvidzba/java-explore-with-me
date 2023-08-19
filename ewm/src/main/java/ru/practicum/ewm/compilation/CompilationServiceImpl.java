package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationInDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;


    @Override
    public CompilationDto addCompilation(CompilationInDto compilationInDto) {
        List<Event> events = eventRepository.findEventByIdIn(compilationInDto.getEvents());
        if (events.size() != compilationInDto.getEvents().size()) {
            throw new ContentAlreadyExistException("Переданные id событий не соответствуют хранящимся данным в базе");
        }
        Compilation compilation = CompilationMapper.mapToCompilation(compilationInDto, events);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.mapToCompilationDto(savedCompilation);
    }

    @Override
    public void removeCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ContentNotFoundException("Подборки с id = " + compId + " не существует"));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto editCompilation(CompilationInDto compilationInDto, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ContentNotFoundException("Подборки с id = " + compId + " не существует"));
        List<Event> events = new ArrayList<>();
        if (compilationInDto.getEvents() != null) {
            events = eventRepository.findEventByIdIn(compilationInDto.getEvents());
            compilation.setEvents(events);
            if (events.size() != compilationInDto.getEvents().size()) {
                throw new ContentAlreadyExistException("Переданные id событий не соответствуют хранящимся данным в базе");
            }
        }
        if (compilationInDto.getPinned() != null) {
            compilation.setPinned(compilationInDto.getPinned());
        }
        if (compilationInDto.getTitle() != null) {
            compilation.setTitle(compilationInDto.getTitle());
        }
        Compilation updatedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.mapToCompilationDto(updatedCompilation);
    }


    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageable);
        return compilations.stream()
                .map(CompilationMapper::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ContentNotFoundException("Подборки с id = " + compId + " не существует"));
        return CompilationMapper.mapToCompilationDto(compilation);
    }
}
