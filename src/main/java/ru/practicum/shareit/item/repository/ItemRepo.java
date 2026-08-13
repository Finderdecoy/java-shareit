package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.itemDto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {

    List<Item> findByOwner(User owner);

    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE ?1 OR LOWER(i.description) LIKE ?1)")
    List<Item> findByDescriptionOrName(String searchQuery);

    @Query("SELECT new ru.practicum.shareit.item.itemDto.ItemDtoWithDate(i.id, i.name, i.description, i.available, " +
            "MAX(CASE WHEN b.bookingStartDate <= CURRENT_TIMESTAMP THEN b.bookingStartDate ELSE NULL END), " +
            "MIN(CASE WHEN b.bookingStartDate > CURRENT_TIMESTAMP THEN b.bookingStartDate ELSE NULL END), " +
            "NULL) " +
            "FROM Item i " +
            "LEFT JOIN i.bookings b " +
            "WHERE i.owner.id = ?1 " +
            "GROUP BY i")
    List<ItemDtoWithDate> findAllWithBookingDates(Long ownerId);

    @Query("SELECT new ru.practicum.shareit.item.itemDto.ItemDtoWithDate(i.id, i.name, i.description, i.available, " +
            "MAX(CASE WHEN b.bookingStartDate <= CURRENT_TIMESTAMP THEN b.bookingStartDate ELSE NULL END), " +
            "MIN(CASE WHEN b.bookingStartDate > CURRENT_TIMESTAMP THEN b.bookingStartDate ELSE NULL END), " +
            "NULL) " +
            "FROM Item i " +
            "LEFT JOIN i.bookings b " +
            "WHERE i.id = ?1 " +
            "GROUP BY i")
    List<ItemDtoWithDate> findItemWithDates(Long itemId);
}
