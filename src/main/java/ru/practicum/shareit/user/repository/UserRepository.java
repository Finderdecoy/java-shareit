package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User create(User user);
    Collection<User> getAllUsers();
    User editUser(User user);
    void deleteUser(Long id);
    Optional<User> getUser(Long id);
}
