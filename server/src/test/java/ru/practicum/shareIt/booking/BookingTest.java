package ru.practicum.shareIt.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareIt.ShareItTests;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Бронирования")
public class BookingTest extends ShareItTests {
    private static String URL = "/bookings";

    @Test
    public void createBookingsValidData() throws Exception {
        Long idUser = getIdFromObject(createUser(user1));
        Long idUser2 = getIdFromObject(createUser(user2));
        Long idItem = getIdFromObject(createItem(item, idUser));
        item.setId(idItem);
        user2.setId(idUser2);

        BookingDtoCreate booking = BookingDtoCreate.builder()
                .itemId(idItem)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", idUser2)
                .content(objectMapper.writeValueAsString(booking))
        ).andExpect(status().isOk());
    }

    @Test
    public void createBookingWhenDateIsBusy() throws Exception {
        Long idUser = getIdFromObject(createUser(user1));
        Long idUser2 = getIdFromObject(createUser(user2));
        Long idItem = getIdFromObject(createItem(item, idUser));
        item.setId(idItem);
        user2.setId(idUser2);

        BookingDtoCreate booking = BookingDtoCreate.builder()
                .itemId(idItem)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", idUser2)
                .content(objectMapper.writeValueAsString(booking))
        );

        long idUser3 = getIdFromObject(createUser(user3));

        booking.setStart(LocalDateTime.now().plusHours(12));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser3)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void selfBooking() throws Exception {
        Long idUser1 = getIdFromObject(createUser(user1));
        Long idItem = getIdFromObject(createItem(item, idUser1));
        item.setId(idItem);

        BookingDtoCreate booking = BookingDtoCreate.builder()
                .itemId(idItem)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", idUser1)
                .content(objectMapper.writeValueAsString(booking))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void approveBookingByOwner() throws Exception {
        Long ownerId = getIdFromObject(createUser(user1));
        Long itemId = getIdFromObject(createItem(item, ownerId));

        Long bookerId = getIdFromObject(createUser(user2));

        BookingDtoCreate bookingDto = BookingDtoCreate.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        ResultActions result = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookerId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAITING"));

        long bookingId = getIdFromObject(result);

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

}

