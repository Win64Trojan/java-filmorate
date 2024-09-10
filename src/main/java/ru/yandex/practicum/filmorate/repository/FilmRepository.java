package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Film addFilm(Film data);

    Film updateFilm(Film data);

    List<Film> getFilmsList();

    Collection<Film> findPopular(int count);

    public Optional<Film> getFilmById(Long id);

    void setLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);


}