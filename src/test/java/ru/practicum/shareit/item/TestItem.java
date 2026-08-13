package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.ShareItTests;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест CRUD операций для Item")
public class TestItem extends ShareItTests {

    public static final String ITEMS = "/items";
    public static final String USER_HEADER = "X-Sharer-User-Id";
    public static final String SEARCH = "/items/search";

    @BeforeEach
    public void beforeEach() throws Exception {
        User user = User.builder()
                .name("Jason")
                .email("jason@mail.ru")
                .build();
        createUser(user);
    }

  /*  @AfterEach
    public void cleanUp() {
        repo.cleanUpForTest();
    }*/

    @Test
    public void shouldCreateItem() throws Exception {
        createRightItem();

        mockMvc.perform(get(ITEMS)
                        .header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Отвертка"));
    }

    private void createRightItem() throws Exception {
        Item item = Item.builder()
                .name("Отвертка")
                .description("Электрическая зряжается от солца")
                .available(true)
                .build();

        createItem(item);
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
                        .header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testCreateItemWithoutName() throws Exception {
        Item item = Item.builder()
                .description("Отвертка механическая")
                .available(true)
                .build();
        createItem(item);

        mockMvc.perform(get(ITEMS)
                        .header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void testCreateItemWithoutAvailable() throws Exception {
        Item item = Item.builder()
                .name("Отвертка")
                .description("Screwdriver mechanical")
                .build();

        createItem(item);

        mockMvc.perform(get(ITEMS).header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testEditItemAvailable() throws Exception {
        createRightItem();

        Item item = Item.builder()
                .available(false)
                .build();

        mockMvc.perform(patch(ITEMS + "/1").header(USER_HEADER, 1)
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

        mockMvc.perform(patch(ITEMS + "/1").header(USER_HEADER, 1)
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

        mockMvc.perform(patch(ITEMS + "/1").header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    public void testSearchAvailableItems() throws Exception {
        createRightItem();

        mockMvc.perform(get(SEARCH).param("text", "отвертка"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Отвертка"));
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
}

