package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

class UserTest {

    @BeforeEach
    void setUp() {
        InMemoryUserStorage.users.clear();
    }

    @Test
    void addingToMapWhenCreated() {
        int creatingTimes = 13;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User();
            UserStorage userStorage = new InMemoryUserStorage();
            user.setEmail("sexyshmeksi@jora.thx");
            user.setLogin("Login");
            user.setName("Name");
            user.setBirthday(LocalDate.of(2000, 6, 11));
            userStorage.addUser(user);
        }

        Assertions.assertEquals(creatingTimes, InMemoryUserStorage.users.size());
    }

    @Test
    void correctIdAllocation() {
        int creatingTimes = 13;
        User lastAddedUser = null;
        for (int i = 0; i < creatingTimes; i++) {
            User user = new User();
            UserStorage userStorage = new InMemoryUserStorage();
            user.setEmail("sexyshmeksi@jora.thx");
            user.setLogin("Login");
            user.setName("Name");
            user.setBirthday(LocalDate.of(2000, 6, 11));
            lastAddedUser = userStorage.addUser(user);
        }

        Assertions.assertEquals(creatingTimes, lastAddedUser.getId());
    }

}