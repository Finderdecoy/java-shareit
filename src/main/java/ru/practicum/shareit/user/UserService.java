package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConfilictData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
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
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto editUser(Long id, User user) {
        checkEmail(user);
        Optional<User> editingUserOpt = userRepository.findById(id);
        if (editingUserOpt.isPresent()) {
            User editingUser = userRepository.findById(id).get();
            if (user.getEmail() != null && !user.getEmail().isBlank()) editingUser.setEmail(user.getEmail());
            if (user.getName() != null && !user.getName().isBlank()) editingUser.setName(user.getName());
            userRepository.save(editingUser);
            return UserMapper.mapToUserDto(editingUser);
        }
        throw new NotFoundException("Пользователь не найден");
    }

    private void checkEmail(User user) {
        if (userRepository.findAll().stream().anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail())))
            throw new ConfilictData("Этот eмейл уже используется");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
