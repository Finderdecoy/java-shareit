package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepo {
    Item createItem(User owner, Item item);

    Collection<Item> getAllItems(Long idUser);

    Optional<Item> getItemById(Long id);

    Item editItem(Item item);

    Collection<Item> searchAvailableItems(String searchQuery);
}
