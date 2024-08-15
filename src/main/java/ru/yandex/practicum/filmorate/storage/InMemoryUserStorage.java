package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    public static final Map<Long, User> users = new HashMap<>();
    HashMap<Long, Set<Long>> userFreindsId = new HashMap<>();


    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя было выдано на основании логина");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с логином, {} ,создан", user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User userNewInfo) {

        if (!users.containsKey(userNewInfo.getId())) {
            log.warn("Пользователь с Id: {} не найден", userNewInfo.getId());
            throw new NotFoundException("Пользователь с Id " + userNewInfo.getId() + " не найден");
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


    @Override
    public Collection<User> getUsersList() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(User user, User friend) {

        Set<Long> uFriends = userFreindsId.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriends.add(friend.getId());

        Set<Long> fFriends = userFreindsId.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.add(user.getId());

    }

    @Override

    public void deleteFriend(User user, User friend) {
        Set<Long> userFriends = userFreindsId.computeIfAbsent(user.getId(), id -> new HashSet<>());
        userFriends.remove(friend.getId());

        Set<Long> fFriends = userFreindsId.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.remove(user.getId());

    }

    @Override
    public List<User> getFriends(User user) {
        Set<Long> userFriends = userFreindsId.get(user.getId());
        List<User> listUser;

        if (userFriends != null) {
            listUser = userFriends.stream()
                    .map(friendId -> getUserById(friendId).get())
                    .collect(Collectors.toList());
        } else {
            listUser = new ArrayList<User>();
        }
        return listUser;
    }

    public List<User> getMutualFriends(User user, User otherUser) {
        Set<Long> result = new HashSet<>(userFreindsId.get(user.getId()));
        Set<Long> resultListOther = userFreindsId.get(otherUser.getId());
        List<User> listMutualFriends;
        if (result != null && resultListOther != null) {
            result.retainAll(resultListOther);

            listMutualFriends = result.stream()
                    .map(userId -> getUserById(userId).get())
                    .collect(Collectors.toList());
        } else {
            listMutualFriends = new ArrayList<User>();
        }

        return listMutualFriends;
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