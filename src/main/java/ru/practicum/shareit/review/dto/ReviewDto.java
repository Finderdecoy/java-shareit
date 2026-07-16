package ru.practicum.shareit.review.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewDto {
    private String ownerName;
    private String comment;
    private LocalDate datePost;
    private String completedTask;
}
