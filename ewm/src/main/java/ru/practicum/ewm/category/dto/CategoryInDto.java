package ru.practicum.ewm.category.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInDto {
    @NotEmpty
    private String name;
}
