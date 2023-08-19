package ru.practicum.ewm.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    @NotEmpty
    @NotBlank
    private String name;
}
