package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.Dto.ItemDto;
import ru.practicum.shareit.item.Dto.ItemDtoOnCreate;
import ru.practicum.shareit.item.commentDto.CommentInDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDtoOnCreate item) {
        log.info("Post item - {} by user - {} ", item, userId);
        return post("", userId, item);
    }

    public ResponseEntity<Object> editItem(Long idUser, Long idItem, ItemDto itemDto) {
        return patch("/" + idItem, idUser, itemDto);
    }

    public ResponseEntity<Object> getItem(Long idItem, Long idUser) {
        return get("/" + idItem, idUser);
    }

    public ResponseEntity<Object> getItemListOwner(Long idUser) {
        return get("", idUser);
    }

    public ResponseEntity<Object> searchAvailableItems(String searchQuery) {

        if (searchQuery.isBlank()) return ResponseEntity.of(Optional.of(List.of()));

        Map<String, Object> param = Map.of("text", searchQuery);

        return get("/search", null, param);
    }

    public ResponseEntity<Object> setComment(Long itemId, Long idUser, CommentInDto comment) {

        return post("http://localhost:8080/" + itemId + "/comments", idUser, comment);
    }
}
