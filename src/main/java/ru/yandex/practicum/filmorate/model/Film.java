package ru.yandex.practicum.filmorate.model;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
//import org.hibernate.validator.constraints.time.DurationMin;
//import ru.yandex.practicum.filmorate.validator.NotBeforeDate;

import java.time.Duration;
import java.time.LocalDate;


@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    long id;
//  @NotBlank
    String name;
//  @Size(max = 200)
    String description;
//  @NotBeforeDate(value = "1895-12-28", message = "Дата релиза не может быть раньше дня создания кинемотографии")
    LocalDate releaseDate;
//  @NotNull
//  @DurationMin
    Duration duration;
    Integer likes;


    public long getDuration() {
        return duration.toSeconds();
    }
}
