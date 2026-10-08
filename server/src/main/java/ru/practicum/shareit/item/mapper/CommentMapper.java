package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.commentDto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment mapToModel(String text, Item item, User user) {
        return Comment.builder()
                .comment(text)
                .item(item)
                .booker(user)
                .postDate(LocalDateTime.now())
                .build();
    }

    public static CommentOutDto mapToOutDto(Comment comment) {
        return CommentOutDto.builder()
                .id(comment.getId())
                .text(comment.getComment())
                .authorName(comment.getBooker().getName())
                .created(comment.getPostDate())
                .build();
    }
}
