package ru.practicum.shareIt.tests.ItemTests;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareIt.tests.ShareItTests;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.item.commentDto.CommentInDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DisplayName("Интеграционные тесты Вещи")
public class TestItem extends ShareItTests {

    public static final String ITEMS = "/items";
    public static final String USER_HEADER = "X-Sharer-User-Id";
    public static final String SEARCH = "/items/search";
    private long userId;
    private long itemId;

    @BeforeEach
    public void beforeEach() throws Exception {
        ResultActions result = createUser(user1);
        userId = getIdFromObject(result);
    }

    private void createRightItem() throws Exception {
        ResultActions result = createItem(item, userId);
        itemId = getIdFromObject(result);
    }

    @Test
    public void testCreateItemWithoutUserId() throws Exception {
        Item item = Item.builder()
                .name("Отвертка")
                .description("Электрическая зряжается от солца")
                .available(true)
                .build();
        createItemOutIdUser(item);

        mockMvc.perform(get(ITEMS)
                        .header(USER_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    @Test
    public void testEditItemAvailable() throws Exception {
        createRightItem();

        Item item = Item.builder()
                .available(false)
                .build();

        mockMvc.perform(patch(ITEMS + "/" + itemId).header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value("false"));

    }

    @Test
    public void testEditItemName() throws Exception {
        createRightItem();

        Item item = Item.builder()
                .name("Update")
                .build();

        mockMvc.perform(patch(ITEMS + "/" + itemId).header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update"));
    }

    @Test
    public void testEditItemDescription() throws Exception {
        createRightItem();

        Item item = Item.builder()
                .description("Updated description")
                .build();

        mockMvc.perform(patch(ITEMS + "/" + itemId).header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    public void testSearchAvailableItems() throws Exception {
        createRightItem();

        mockMvc.perform(get(SEARCH).param("text", "Screw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ScrewDriver"));
    }

    @Test
    public void testSearchWithEmptyQuery() throws Exception {
        createRightItem();

        mockMvc.perform(get(SEARCH).param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testSearchWhenToolNotAvailable() throws Exception {
        Item item = Item.builder()
                .name("test")
                .description("test data")
                .available(false)
                .build();

        mockMvc.perform(get(SEARCH).param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testWriteCommentRentedItem() throws Exception {
        createRightItem();
        long bookerId = getIdFromObject(createUser(user2));

        BookingDtoCreate bookingDto = BookingDtoCreate.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        ResultActions resultBooking = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookerId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(resultBooking);

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        CommentInDto commentDto = CommentInDto.builder()
                .text("Дрель просто пушка! Отверстия в стене как по маслу.")
                .build();

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookerId)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Дрель просто пушка! Отверстия в стене как по маслу."))
                .andExpect(jsonPath("$.authorName").value("Petr"));
    }

    @Test
    public void testWriteCommentNonRentedItem() throws Exception {
        createRightItem();
        long bookerId = getIdFromObject(createUser(user2));

        CommentInDto commentDto = CommentInDto.builder()
                .text("Даже не пользовался, но хочу написать гневный коммент!")
                .build();

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookerId)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }
}
