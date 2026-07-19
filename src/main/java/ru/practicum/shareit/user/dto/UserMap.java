package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMap {
    public static UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setDateRegistration(user.getDateRegistration());
        return userDto;
    }

    public static User map(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
