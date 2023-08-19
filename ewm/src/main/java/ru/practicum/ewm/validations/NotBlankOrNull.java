package ru.practicum.ewm.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = NotBlankOrNullValidator.class)
@Documented
public @interface NotBlankOrNull {

    String message() default "Имя не должно быть пустым";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}