package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> getUsersList() {
        return userStorage.getUsersList();
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    @Override
    public User getUserById(long id) {
        final User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с " + id + "не найден"));
        return user;
    }

    @Override
    public List<User> getFriends(long id) {

        return userStorage.getFriends(getUserById(id));
    }

    @Override
    public void addFriend(long id, long friendId) {
        userStorage.addFriend(getUserById(id), getUserById(friendId));
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        userStorage.deleteFriend(getUserById(id), getUserById(friendId));
    }

    @Override
    public List<User> getMutualFriends(long id, long otherId) {
        return userStorage.getMutualFriends(getUserById(id), getUserById(otherId));
    }
}