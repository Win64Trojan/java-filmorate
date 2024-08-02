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
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public static final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        log.debug("Запрос на создание пользователя");
        User newUser = UserController.add(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User userNewInfo) {
        log.debug("Запрос на обновление существующего в базе пользователя");
        User updatedUser = UserController.update(userNewInfo);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsersList() {
        Collection<User> users = UserController.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public static User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя было выдано на основании логина");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с логином, {} ,создан", user.getLogin());
        return user;
    }

    public static User update(User userNewInfo) {
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

    public static Collection<User> getUsers() {
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
