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
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {


    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        log.debug("Запрос на создание пользователя");
        User newUser = User.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User userNewInfo) {
        log.debug("Запрос на обновление существующего в базе пользователя");
        User updatedUser = User.updateUser(userNewInfo);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsersList() {
        Collection<User> users = User.getUsersList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
