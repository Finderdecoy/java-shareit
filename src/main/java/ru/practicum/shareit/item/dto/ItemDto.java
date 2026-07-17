package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.review.model.Review;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    //private String available;
    private Boolean available;
    private List<Review> reviews;
}
