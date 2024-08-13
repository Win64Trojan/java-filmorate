package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.NotContains;

import java.time.LocalDate;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    long id;
    @Email
    @NotEmpty
    String email;
    @NotBlank
    @NotContains(value = " !@#$%^&*()_+|<,.>:;'[]{}-=")
    String login;
    String name;
    @Past
    LocalDate birthday;
}
