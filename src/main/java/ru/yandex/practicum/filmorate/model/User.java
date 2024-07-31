package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.validator.NotContains;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    public static final Map<Long, User> users = new HashMap<>();

    long id;
    @Email
    @NotEmpty
    String email;
    @NotBlank
    @NotContains(value = " !@#$%^&*()_+|<,.>:;'[]{}-=")
    String login;
    String name;
    @Past
    LocalDate birthday;


    public static User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя было выдано на основании логина");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с логином, {} ,создан", user.getLogin());
        return user;
    }

    public static User updateUser(User userNewInfo) {
        if (!users.containsKey(userNewInfo.getId())) {
            log.warn("Пользователь с Id: {} не найден", userNewInfo.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        long id = userNewInfo.getId();
        User user = users.get(id);
        user.setLogin(Objects.requireNonNullElse(userNewInfo.getLogin(), user.getLogin()));
        user.setName(Objects.requireNonNullElse(userNewInfo.getName(), user.getName()));
        user.setEmail(userNewInfo.getEmail());
        if (user.getBirthday() != null) {
            user.setBirthday(Objects.requireNonNullElse(userNewInfo.getBirthday(), user.getBirthday()));
        } else {
            user.setBirthday(userNewInfo.getBirthday());
        }
        log.info("Пользователь с Id:{} - обновлен, Логин пользователя:{}", user.getId(), user.getLogin());
        return user;
    }

    public static Collection<User> getUsersList() {
        return users.values();
    }

    private static long getNextId() {
        log.trace("Генерация нового Id");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Присвоен новый Id:", (currentMaxId + 1));
        return ++currentMaxId;
    }
}
