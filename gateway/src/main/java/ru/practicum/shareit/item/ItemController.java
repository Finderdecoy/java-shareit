package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Dto.ItemDto;
import ru.practicum.shareit.item.Dto.ItemDtoOnCreate;
import ru.practicum.shareit.item.commentDto.CommentInDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                                             @Validated @RequestBody ItemDtoOnCreate itemDto) {
        log.info("Request to add item - {}", itemDto);
        return itemClient.createItem(idUser, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editItem(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                                           @NotNull @Positive @PathVariable Long id,
                                           @Validated @RequestBody ItemDto itemDto) {
        return itemClient.editItem(idUser, id, itemDto);
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<Object> getItemById(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                                              @PathVariable Long idItem) {
        return itemClient.getItem(idItem, idUser);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser) {
        return itemClient.getItemListOwner(idUser);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByNameAndDescrip(@RequestParam(name = "text") String searchQuery) {
        return itemClient.searchAvailableItems(searchQuery);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> setComment(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentInDto comment) {
        log.info("Setting comment to item {} , by user {}", itemId, idUser);
        return itemClient.setComment(itemId, idUser, comment);
    }
}
