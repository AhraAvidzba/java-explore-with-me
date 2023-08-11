package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.validations.Create;
import ru.practicum.ewm.validations.EmailOrNull;
import ru.practicum.ewm.validations.NotBlankOrNull;
import ru.practicum.ewm.validations.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserInDto {
    @NotBlankOrNull
    private String name;
    @NotNull(groups = Create.class)
    @Email(groups = Create.class)
    @EmailOrNull(groups = Update.class)
    private String email;
}
