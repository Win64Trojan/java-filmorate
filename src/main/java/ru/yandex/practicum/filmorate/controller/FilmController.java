package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public static final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        Film newFilm = FilmController.add(film);
        return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film filmNewInfo) {
        log.debug("Запрос на обновление данных о фильме");
        Film updatedFilm = FilmController.update(filmNewInfo);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilmsList() {
        Collection<Film> allFilms = FilmController.getFilms();
        return new ResponseEntity<>(allFilms, HttpStatus.OK);
    }

    public static Film add(@RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм - Id фильма:{}, Название фильма:{}", film.getId(), film.getName());
        return film;
    }

    public static Film update(@RequestBody Film filmNewInfo) {
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

    public static Collection<Film> getFilms() {
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

