package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationInDto;

import java.util.List;

public interface CompilationService {
    //Методы для админов
    CompilationDto addCompilation(CompilationInDto compilationInDto);

    void removeCompilation(Long compId);

    CompilationDto editCompilation(CompilationInDto compilationInDto, Long compId);

    //Публичные методы
    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);
}
