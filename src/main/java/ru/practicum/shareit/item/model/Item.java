package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.review.model.Review;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
public class Item {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    private User owner;
    @NotNull
    private Boolean available;
    private List<Review> reviews;
}
