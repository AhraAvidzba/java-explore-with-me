package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationInDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerAdmin {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody CompilationInDto compilationInDto) {
        CompilationDto compilationDto = compilationService.addCompilation(compilationInDto);
        log.info("Подборка добавлена");
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Long compId) {
        log.info("Подборка удаляется");
        compilationService.removeCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CompilationDto editCompilation(@RequestBody CompilationInDto compilationInDto,
                                          @PathVariable Long compId) {
        log.info("Подборка обновляется");
        return compilationService.editCompilation(compilationInDto, compId);
    }

}
