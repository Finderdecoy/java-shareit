package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.ShareItTests;
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Запросов")
public class RequestItemTest extends ShareItTests {
    public static final String URL_TEMPLATE = "/requests";
    protected long requestId;

    @Test
    public void createRequest() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        requestCreate(idUser);
    }

    private void requestCreate(long idUser) throws Exception {
        ItemRequestOnCreate request = ItemRequestOnCreate.builder()
                .description("Эта вещь должна крутить саморезы")
                .build();

        var result = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        requestId = getIdFromObject(result);
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
        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getMyRequestWhenAreHaveOne() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        requestCreate(idUser);
        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", idUser))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGettingRequestById() throws Exception {
        long idUser = getIdFromObject(createUser(user1));
        requestCreate(idUser);

        mockMvc.perform(get(URL_TEMPLATE + "/" + requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId));

    }
}
