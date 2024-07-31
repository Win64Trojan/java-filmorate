package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.validator.NotBeforeDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    public static final Map<Long, Film> films = new HashMap<>();

    long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotBeforeDate(value = "1895-12-28", message = "Дата релиза не может быть раньше дня создания кинемотографии")
    LocalDate releaseDate;
    @NotNull
    @DurationMin
    Duration duration;

    public long getDuration() {
        return duration.toSeconds();
    }


    public static Film addFilm(@RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм - Id фильма:{}, Название фильма:{}", film.getId(), film.getName());
        return film;
    }

    public static Film updateFilm(@RequestBody Film filmNewInfo) {
        if (!films.containsKey(filmNewInfo.getId())) {
            log.warn("Запрашиваемый фильм по Id: {} не найден", filmNewInfo.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Film film = films.get(filmNewInfo.getId());
        film.setName(Objects.requireNonNullElse(filmNewInfo.getName(), film.getName()));
        film.setDescription(Objects.requireNonNullElse(filmNewInfo.getDescription(), film.getDescription()));
        film.setReleaseDate(Objects.requireNonNullElse(filmNewInfo.getReleaseDate(), film.getReleaseDate()));
        film.setDuration(Objects.requireNonNullElse(Duration.ofSeconds(filmNewInfo.getDuration()),
                Duration.ofSeconds(film.getDuration())));
        log.info("Обновлены данные о фильме по Id:{}", film.getId());
        return film;
    }

    public static Collection<Film> getFilmsList() {
        return films.values();
    }

    private static long getNextId() {
        log.trace("Генерация нового Id");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Присвоен новый Id:{}", (currentMaxId + 1));
        return ++currentMaxId;
    }
}
