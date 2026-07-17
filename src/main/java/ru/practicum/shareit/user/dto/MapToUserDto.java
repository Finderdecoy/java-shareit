package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class MapToUserDto {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setDateRegistration(user.getDateRegistration());
        return userDto;
    }
}
