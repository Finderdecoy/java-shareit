package ru.practicum.shareIt.tests.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareIt.tests.ShareItTests;
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Запросов")
public class RequestItemTest extends ShareItTests {

    @Test
    public void createRequest() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        requestCreate(idUser);
    }

    private void requestCreate(long idUser) throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .description("Эта вещь должна крутить саморезы")
                .build();

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllRequest() throws Exception {
        createRequest();
        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void getMyRequestWhenAreNone() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getMyRequestWhenAreHaveOne() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        requestCreate(idUser);
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
