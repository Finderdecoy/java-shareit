package ru.practicum.shareit.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.practicum.shareit.ShareItTests;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserMemRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тесты CRUD операций пользователей")
class TestUsers extends ShareItTests {
    @Autowired
    public UserMemRepo repo;

    @AfterEach
    public void cleanUp() {
        repo.cleanUpForTest();
    }

    @Test
    public void testCreateRightUser() throws Exception {
        User user = User.builder()
                .name("Пользователь 1")
                .email("mail@email.ru")
                .build();
        createUser(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    public void testCreateUserWithoutName() throws Exception {
        User user = User.builder()
                .email("mail@email.ru")
                .build();
        createUser(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void createUserWithoutEmail() throws Exception {
        User user = User.builder()
                .name("Пользователь 1")
                .build();
        createUser(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void createUserWithWrongEmail() throws Exception {
        User user = User.builder()
                .name("Пользователь 1")
                .email("mailw.ru")
                .build();
        createUser(user);


        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testEditUser() throws Exception {
        User user = User.builder()
                .name("Monica")
                .email("mail@mail.ru")
                .build();
        createUser(user);

        User editUser = User.builder()
                .id(1L)
                .name("Update User")
                .email("update@mail.ru")
                .build();


        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update User"))
                .andExpect(jsonPath("$.email").value("update@mail.ru"));
    }

    @Test
    public void testWhenMailExisting() throws Exception {
        User user = User.builder()
                .name("Monica")
                .email("mail@mail.ru")
                .build();
        createUser(user);

        User editUser = User.builder()
                .id(1L)
                .email("mail@mail.ru")
                .build();


        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("Этот eмейл уже используется"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = User.builder()
                .name("Monica")
                .email("mail@mail.ru")
                .build();
        createUser(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
