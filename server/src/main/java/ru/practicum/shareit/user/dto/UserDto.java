package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
