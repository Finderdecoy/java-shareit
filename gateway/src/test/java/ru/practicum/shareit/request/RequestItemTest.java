package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты валидации для Запроса")
@WebMvcTest(controllers = ItemRequestController.class)
public class RequestItemTest {
    @Autowired
    private MockMvc mock;

    @Autowired
    private RequestClient requestClient;

    @TestConfiguration
    static class BookingTestConfig {
        @Bean
        public RequestClient bookingClient() {
            return Mockito.mock(RequestClient.class);
        }
    }

    @Autowired
    private ObjectMapper mapper;

    private static final Long USER_ID = 2L;

    @Test
    public void createRequestWhenDtoValid() throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .description("Крестовая ответрка")
                .build();

        mock.perform(post("/requests")
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void createRequestWhenDtoNull() throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .build();

        mock.perform(post("/requests")
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createRequestWhenDtoBlank() throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .description(" ")
                .build();

        mock.perform(post("/requests")
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createRequestWhenMissingUserHeader() throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .description("Крестовая ответрка")
                .build();

        mock.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
