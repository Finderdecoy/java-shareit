package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты валидации Пользователей")
@WebMvcTest(controllers = UserController.class)
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserClient userClient;

    @TestConfiguration
    static class BookingTestConfig {
        @Bean
        public UserClient bookingClient() {
            return Mockito.mock(UserClient.class);
        }
    }

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testWhenUserNameIsNull() throws Exception {
        UserDto testUser = UserDto.builder()
                .name(null)
                .email("Saho@mail.ru")
                .build();

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhenUserNameIsBlank() throws Exception {
        UserDto testUser = UserDto.builder()
                .name("")
                .email("Saho@mail.ru")
                .build();

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhenUserEmailIsNull() throws Exception {
        UserDto nullMail = UserDto.builder()
                .name("John")
                .email(null)
                .build();

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nullMail))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhenUserEmailIsBlank() throws Exception {
        UserDto blankMail = UserDto.builder()
                .name("John")
                .email("")
                .build();

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(blankMail))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}