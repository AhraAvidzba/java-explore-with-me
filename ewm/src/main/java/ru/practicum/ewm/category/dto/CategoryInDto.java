package ru.practicum.ewm.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInDto {
//    private Long id;
    @NotEmpty
    private String name;
}
