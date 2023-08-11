package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.validations.Create;
import ru.practicum.ewm.validations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CompilationInDto {
    @NotNull(groups = Create.class)
    private List<Long> events;
    @NotNull(groups = Create.class)
    private Boolean pinned;
    @NotBlank(groups = Create.class)
    private String title;
}
