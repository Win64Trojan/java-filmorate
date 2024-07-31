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
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        Film newFilm = Film.addFilm(film);
        return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film filmNewInfo) {
        log.debug("Запрос на обновление данных о фильме");
        Film updatedFilm = Film.updateFilm(filmNewInfo);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilmsList() {
        Collection<Film> allFilms = Film.getFilmsList();
        return new ResponseEntity<>(allFilms, HttpStatus.OK);
    }

}
