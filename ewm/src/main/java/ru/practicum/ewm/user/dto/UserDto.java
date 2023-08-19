package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Long> subscribers;
}
