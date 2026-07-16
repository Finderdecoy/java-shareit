package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NotNull
    private String name;
    private String login;
    private String password;
    @NotNull
    @Email
    private String email;
    private LocalDate dateRegistration;
}
