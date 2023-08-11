package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents().stream().map(EventMapper::mapToEventShortDto).collect(Collectors.toList()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .id(compilation.getId())
                .build();
    }

    public static Compilation mapToCompilation(CompilationInDto compilationInDto, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(compilationInDto.getPinned())
                .title(compilationInDto.getTitle())
                .build();
    }
}
