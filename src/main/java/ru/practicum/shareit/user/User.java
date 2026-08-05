package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private LocalDate dateRegistration;
}
