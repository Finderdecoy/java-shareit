package ru.practicum.shareIt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ShareItTests {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static User user1;
    protected static User user2;
    protected static User user3;
    protected static Item item;

    @BeforeAll
    protected static void prepareData() {
        user1 = User.builder()
                .name("Jon")
                .email("jon@ya.ru")
                .build();

        user2 = User.builder()
                .name("Petr")
                .email("petr@ya.ru")
                .build();

        user3 = User.builder()
                .name("Gosha")
                .email("gosha@mail.ru")
                .build();

        item = Item.builder()
                .name("ScrewDriver")
                .description("Some do with screws")
                .available(true)
                .build();
    }

    protected Long getIdFromObject(ResultActions response) throws Exception {
        return JsonPath.parse(response
                .andReturn()
                .getResponse()
                .getContentAsString()).read("$.id", Long.class);
    }

    protected ResultActions createUser(User user) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
    }

    protected ResultActions createItem(Item item, Long userId) throws Exception {
        return mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

    protected ResultActions createItemOutIdUser(Item item) throws Exception {
        return mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

}