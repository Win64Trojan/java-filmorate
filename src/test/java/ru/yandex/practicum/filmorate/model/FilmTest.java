package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;

class FilmTest {

    @BeforeEach
    void setUp() {
        InMemoryFilmStorage.films.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = new Film();
            FilmStorage filmStorage = new InMemoryFilmStorage();
            film.setName("Name");
            film.setDescription("Description");
            film.setReleaseDate(LocalDate.of(2000, 6, 11));
            film.setDuration(Duration.ofSeconds(10));
            filmStorage.addFilm(film);
        }

        Assertions.assertEquals(creatingTimes, InMemoryFilmStorage.films.size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        Film lastAddedFilm = null;
        for (int i = 0; i < creatingTimes; i++) {
            Film film = new Film();
            FilmStorage filmStorage = new InMemoryFilmStorage();
            film.setName("Name");
            film.setDescription("Description");
            film.setReleaseDate(LocalDate.of(2000, 6, 11));
            film.setDuration(Duration.ofSeconds(10));
            lastAddedFilm = filmStorage.addFilm(film);
        }

        Assertions.assertEquals(creatingTimes, lastAddedFilm.getId());
    }

}