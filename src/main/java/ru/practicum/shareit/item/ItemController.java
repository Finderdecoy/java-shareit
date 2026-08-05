package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOnCreate;
import ru.practicum.shareit.item.dto.ItemMap;
import ru.practicum.shareit.item.model.Item;

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
    public ItemDto getItemById(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public Collection<ItemDto> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id", required = true) Long idUser) {
        return itemService.getItemList(idUser);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByNameAndDescrip(@RequestParam(name = "text") String searchQuery) {
        if (searchQuery.isBlank()) return List.of();
        return itemService.searchAvailableItems(searchQuery);
    }

}
