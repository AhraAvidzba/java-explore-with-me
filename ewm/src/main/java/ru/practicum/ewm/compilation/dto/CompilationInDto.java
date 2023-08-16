package ru.practicum.ewm.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.validations.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompilationInDto {
    private List<Long> events = new ArrayList<>();
    @NotNull(groups = Create.class)
    private Boolean pinned = false;
    @NotBlank(groups = Create.class)
    @Length(min = 1, max = 50)
    private String title;
}
