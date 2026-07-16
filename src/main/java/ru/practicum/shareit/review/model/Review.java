package ru.practicum.shareit.review.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class Review {
    private Long id;
    private User ownerReview;
    private String comment;
    private LocalDate datePost;
    private Boolean completedTask;
}
