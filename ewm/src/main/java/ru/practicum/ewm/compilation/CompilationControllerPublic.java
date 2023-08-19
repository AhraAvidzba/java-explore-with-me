package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerPublic {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {
        List<CompilationDto> compilationDto = compilationService.getCompilations(pinned, from, size);
        log.info("Подборки возвращаются. Эндпоинт {}", request.getRequestURI());
        return compilationDto;
    }

    @GetMapping("/{compId}")
    public CompilationDto removeCompilation(@PathVariable Long compId,
                                            HttpServletRequest request) {
        log.info("Подборка возвращается. Эндпоинт {}", request.getRequestURI());
        return compilationService.getCompilationById(compId);
    }

}
