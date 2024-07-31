package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserTest {

    @BeforeEach
    void setUp() {
        User.users.clear();
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
            User.addUser(user);
        }

        Assertions.assertEquals(creatingTimes, User.getUsersList().size());
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
            lastAddedUser = User.addUser(user);
        }

        Assertions.assertEquals(creatingTimes, lastAddedUser.getId());
    }

}