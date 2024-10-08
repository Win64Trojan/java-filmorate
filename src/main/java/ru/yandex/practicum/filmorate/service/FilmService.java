package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Service
public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film filmNewInfo);

    Film getFilmById(long id);

    Collection<Film> getFilmsList();

    Collection<Film> findPopular(int count);

    void setLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}