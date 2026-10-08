package ru.practicum.shareIt.UserTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareIt.ShareItTests;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Пользователь")
public class TestUsers extends ShareItTests {

    @Test
    public void testEditUser() throws Exception {
        long userId = getIdFromObject(createUser(user1));
        User editUser = User.builder()
                .id(userId)
                .name("Update User")
                .email("update@mail.ru")
                .build();


        mockMvc.perform(patch("/users/" + userId)
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
        long idUser = getIdFromObject(createUser(user3));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(delete("/users/" + idUser))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}