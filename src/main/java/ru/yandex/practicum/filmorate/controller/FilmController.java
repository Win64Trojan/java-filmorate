package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final ValidateService validateService;


    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        validateService.validateFilm(film);
        return new ResponseEntity<>(filmService.addFilm(film), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film filmNewInfo) {
        log.debug("Запрос на обновление данных о фильме");
        validateService.validateFilm(filmNewInfo);
        return new ResponseEntity<>(filmService.updateFilm(filmNewInfo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilmsList() {
        return new ResponseEntity<>(filmService.getFilmsList(), HttpStatus.OK);
    }

    @GetMapping("popular")
    public Collection<Film> findPopular(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findPopular(count);
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable long id) {
        log.debug("Запрос на получения фильма по айди");
        return filmService.getFilmById(id);
    }

    @PutMapping("{id}/like/{user-id}")
    public void setLike(@PathVariable long id, @PathVariable("user-id") long userId) {
        log.debug("Запрос на получение  данных о лайках");
        filmService.setLike(id, userId);
    }

    @DeleteMapping("{id}/like/{user-id}")
    public void deleteLink(@PathVariable long id, @PathVariable("user-id") long userId) {
        filmService.deleteLike(id, userId);
    }
}

