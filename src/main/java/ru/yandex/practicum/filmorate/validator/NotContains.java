package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {NotContainsValidator.class}
)
public @interface NotContains {

    String message() default "Содержит недопустимые символы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}
