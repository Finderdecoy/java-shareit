package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Dto.ItemDtoOnCreate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName(value = "Тест валидации класса ItemController")
@WebMvcTest(controllers = ItemController.class)
public class TestItemsRest {
    @Autowired
    protected MockMvc mock;

    @MockBean
    protected ItemClient mockItem;

    @Autowired
    protected ObjectMapper mapper;

    @Test
    public void testAddItemWhenNameNull() throws Exception {
        ItemDtoOnCreate createItem = ItemDtoOnCreate.builder()
                .description("test descriptions")
                .available(true)
                .build();

        String jsonItem = mapper.writeValueAsString(createItem);
        mock.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(1l))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddItemWhenNameIsBlank() throws Exception {
        ItemDtoOnCreate createItem = ItemDtoOnCreate.builder()
                .name("")
                .description("test descriptions")
                .available(true)
                .build();

        String jsonItem = mapper.writeValueAsString(createItem);
        mock.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(1l))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddItemWithoutDescription() throws Exception {
        ItemDtoOnCreate createItem = ItemDtoOnCreate.builder()
                .name("Name")
                .description("")
                .available(true)
                .build();

        String jsonItem = mapper.writeValueAsString(createItem);
        mock.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(1l))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddItemWithoutAvailable() throws Exception {
        ItemDtoOnCreate createItem = ItemDtoOnCreate.builder()
                .name("Name")
                .description("Some description")
                .build();

        String jsonItem = mapper.writeValueAsString(createItem);
        mock.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(1l))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isBadRequest());
    }
}
