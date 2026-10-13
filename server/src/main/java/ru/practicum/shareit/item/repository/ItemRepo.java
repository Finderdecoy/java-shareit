package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE ?1 OR LOWER(i.description) LIKE ?1)")
    List<Item> findByDescriptionOrName(String searchQuery);

    List<Item> findByOwnerId(Long idUser);
}
