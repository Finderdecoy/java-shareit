package ru.practicum.shareIt.tests.UserTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareIt.tests.ShareItTests;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Пользователь")
public class TestUsers extends ShareItTests {

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

        mockMvc.perform(delete("/users/3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}