package ru.practicum.shareit.review.dto;

import ru.practicum.shareit.review.model.Review;

public class MapToReviewDto {
    public ReviewDto mapToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOwnerName(review.getOwnerReview().getName());
        reviewDto.setComment(review.getComment());
        reviewDto.setDatePost(review.getDatePost());
        reviewDto.setCompletedTask(review.getCompletedTask() ? "Справился с задачей" : "Не справился с задачей");
        return reviewDto;
    }
}
