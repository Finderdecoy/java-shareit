package ru.practicum.shareit.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate dateRegistration;
}
