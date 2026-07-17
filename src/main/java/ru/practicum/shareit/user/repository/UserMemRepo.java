package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserMemRepo implements UserRepository {
    private final Map<Long, User> users = new HashMap();

    @Override
    public User create(User user) {
        Long id = generateId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User editUser(User editingUser) {
        users.put(editingUser.getId(), editingUser);
        return users.get(editingUser.getId());
    }

    @Override
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) throw new NotFoundException("Пользователь с id : " + id + " не найден");
        users.remove(id);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private Long generateId() {
        return (long) users.size() + 1;
    }

    public void cleanUpForTest() {
        users.clear();
    }
}
