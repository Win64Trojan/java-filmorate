package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;

class UserTest {

    @BeforeEach
    void setUp() {
        UserController.users.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User();
            user.setEmail("sexyshmeksi@jora.thx");
            user.setLogin("Login");
            user.setName("Name");
            user.setBirthday(LocalDate.of(2000, 6, 11));
            UserController.add(user);
        }

        Assertions.assertEquals(creatingTimes, UserController.getUsers().size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        User lastAddedUser = null;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User();
            user.setEmail("sexyshmeksi@jora.thx");
            user.setLogin("Login");
            user.setName("Name");
            user.setBirthday(LocalDate.of(2000, 6, 11));
            lastAddedUser = UserController.add(user);
        }

        Assertions.assertEquals(creatingTimes, lastAddedUser.getId());
    }

}