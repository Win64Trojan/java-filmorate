package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {

    public User addUser(User user);

    public User updateUser(User userNewInfo);

    public Optional<User> getUserById(long id);

    Collection<User> getUsersList();

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getMutualFriends(User user, User otherUser);
}
