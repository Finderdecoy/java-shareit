package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты валидации Аренды")
@WebMvcTest(controllers = BookingController.class)
public class BookingTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingClient bookingClient;

    private static final Long ITEM_ID = 1L;
    private static final Long USER_ID = 2L;

    @Test
    public void createBookingWhenValidDto() throws Exception {
        BookItemRequestDto validDto = BookItemRequestDto.builder()
                .itemId(ITEM_ID)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testBookingWhenItemIdIsNull() throws Exception {
        BookItemRequestDto invalidDto = BookItemRequestDto.builder()
                .itemId(null)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookingWhenStartIsInPast() throws Exception {
        BookItemRequestDto invalidDto = BookItemRequestDto.builder()
                .itemId(ITEM_ID)
                .start(LocalDateTime.now().minusDays(1)) // Ошибка: старт в прошлом
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookingWhenEndIsBeforeStart() throws Exception {
        BookItemRequestDto invalidDto = BookItemRequestDto.builder()
                .itemId(ITEM_ID)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookingWhenMissingUserHeader() throws Exception {
        BookItemRequestDto validDto = BookItemRequestDto.builder()
                .itemId(ITEM_ID)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest());
    }
}
