package ru.practicum.shareIt.tests.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareIt.tests.ShareItTests;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookingTest extends ShareItTests {
    private static String URL = "/bookings";
    private User user;
    private User user2;
    private Item item;

    @BeforeEach
    void prepareData() {
        user = User.builder()
                .name("Jon")
                .email("jon@ya.ru")
                .build();

        user2 = User.builder()
                .name("Petr")
                .email("petr@ya.ru")
                .build();

        item = Item.builder()
                .name("ScrewDriver")
                .description("Some do with screws")
                .available(true)
                .build();
    }

    @Test
    public void createBookingsValidData() throws Exception {
        Long idUser = getIdFromObject(createUser(user));
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

}
