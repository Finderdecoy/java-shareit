/*
package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ShareItTests {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void checkValidationError(ResultActions response, String expMessage) throws Exception {
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(expMessage));
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

    protected ResultActions createItem(Item item) throws Exception {
        return mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

    protected ResultActions createItemOutIdUser(Item item) throws Exception {
        return mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

}
*/
