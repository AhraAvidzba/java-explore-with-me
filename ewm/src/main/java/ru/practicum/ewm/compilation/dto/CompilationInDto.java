package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CompilationInDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
