package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage usertorage;

    @Override
    public Collection<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film filmNewInfo) {
        return filmStorage.updateFilm(filmNewInfo);
    }

    @Override
    public Collection<Film> findPopular(int count) {
        return filmStorage.findPopular(count);
    }

    @Override
    public Film getFilmById(long id) {
        final Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с " + id + "не найден"));

        return film;
    }

    @Override
    public void setLike(long filmId, long userId) {
        User user = usertorage.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + "не найден"));
        filmStorage.setLike(getFilmById(filmId), user);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        User user = usertorage.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + "не найден"));
        filmStorage.deleteLike(getFilmById(filmId), user);
    }

}