package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ItemMemRepo implements ItemRepo {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(User owner, Item item) {
        item.setId(generateId());
        item.setOwner(owner);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> getAllItems(Long idUser) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(idUser))
                .toList();
    }

    @Override
    public Optional<Item> getItemById(Long idItem) {
        log.info("Поиск в репозитории вещи с id = {}", idItem);
        log.info("Список вещей {}", items);
        return Optional.ofNullable(items.get(idItem));
    }

    @Override
    public Item editItem(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Collection<Item> searchAvailableItems(String searchQuery) {
        return items.values().stream()
                .filter(item -> item.getAvailable() == true && (
                        item.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(searchQuery.toLowerCase())))
                .toList();
    }

    private Long generateId() {
        return (long) items.size() + 1;
    }

    public void cleanUpForTest() {
        items.clear();
    }

}
