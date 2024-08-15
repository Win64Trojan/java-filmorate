package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    public static final Map<Long, Film> films = new HashMap<>();
    HashMap<Long, Set<Long>> usersLike = new HashMap<>();

    public Collection<Film> getFilmsList() {
        return films.values();

    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм - Id фильма:{}, Название фильма:{}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film filmNewInfo) {
        if (!films.containsKey(filmNewInfo.getId())) {
            log.warn("Запрашиваемый фильм по Id: {} не найден", filmNewInfo.getId());
            throw new NotFoundException("Запрашиваемый фильм по Id " + filmNewInfo.getId() + " не найден");
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

    public Collection<Film> findPopular(int count) {
        return films.values().stream()
                .sorted((Film f1, Film f2) -> {
                    Integer compare = f1.getLikes().compareTo(f2.getLikes());

                    return compare * (-1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<Film> getFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    public void setLike(Film film, User user) {
        Set<Long> setUsersLike = usersLike.get(film.getId());
        Integer countLike = film.getLikes();

        if (countLike != null) {
            film.setLikes(countLike + 1);
        } else {
            film.setLikes(1);
        }

        if (setUsersLike == null) {
            setUsersLike = new HashSet<Long>();

        }
        setUsersLike.add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        Set<Long> setUsersLike = usersLike.get(film.getId());

        if (film.getLikes() > 1) {
            film.setLikes(film.getLikes() - 1);
            if (setUsersLike.contains(user.getId())) {
                setUsersLike.remove(user.getId());
            }
        }
    }

    public int getNumLike(long id) {
        return usersLike.get(id).size();
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