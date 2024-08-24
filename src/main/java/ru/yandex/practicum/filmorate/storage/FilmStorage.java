package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(@RequestBody Film filmNewInfo);

    Optional<Film> getFilmById(long id);

    Collection<Film> getFilmsList();

    Collection<Film> findPopular(int count);

    void setLike(Film film, User user);

    void deleteLike(Film film, User user);

    int getNumLike(long id);
}
