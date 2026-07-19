package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConfilictData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMap;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(User user) {
        checkEmail(user);
        return UserMap.map(userRepository.create(user));
    }

    public List<UserDto> getUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMap::map)
                .toList();
    }

    public UserDto editUser(Long id, User user) {
        checkEmail(user);
        Optional<User> editingUserOpt = userRepository.getUser(id);
        if (editingUserOpt.isPresent()) {
            User editingUser = userRepository.getUser(id).get();
            if (user.getEmail() != null && !user.getEmail().isBlank()) editingUser.setEmail(user.getEmail());
            if (user.getName() != null && !user.getName().isBlank()) editingUser.setName(user.getName());
            if (user.getLogin() != null && !user.getLogin().isBlank()) editingUser.setLogin(user.getLogin());
            if (user.getPassword() != null && !user.getPassword().isBlank())
                editingUser.setPassword(user.getPassword());
            userRepository.editUser(editingUser);
            return UserMap.map(editingUser);
        }
        throw new NotFoundException("Пользователь не найден");
    }

    private void checkEmail(User user) {
        if (userRepository.getAllUsers().stream().anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail())))
            throw new ConfilictData("Этот eмейл уже используется");
    }

    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    public UserDto getUser(Long id) {
        return userRepository.getUser(id)
                .map(UserMap::map)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
