package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.commentDto.CommentInDto;
import ru.practicum.shareit.item.commentDto.CommentOutDto;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemDtoOnCreate;
import ru.practicum.shareit.item.itemDto.ItemDtoWithDate;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                              @Validated @RequestBody ItemDtoOnCreate itemDto) {
        Item item = ItemMap.mapToItemOnCreate(itemDto);
        return itemService.createItem(idUser, item);
    }

    @PatchMapping("/{id}")
    public ItemDto editItem(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                            @PathVariable Long id,
                            @RequestBody ItemDto itemDto) {
        Item item = ItemMap.mapToItem(itemDto);
        return itemService.editItem(idUser, id, item);
    }

    @GetMapping("/{id}")
    public ItemDtoWithDate getItemById(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public Collection<ItemDtoWithDate> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser) {
        return itemService.getItemList(idUser);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByNameAndDescrip(@RequestParam(name = "text") String searchQuery) {
        if (searchQuery.isBlank()) return List.of();
        return itemService.searchAvailableItems(searchQuery);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutDto setComment(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentInDto comment) {
        return itemService.setComment(itemId, idUser, comment);
    }

}
