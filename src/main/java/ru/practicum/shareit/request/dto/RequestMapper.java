package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {

    public static ItemRequest toCreate(ItemRequestOnCreate dto, User user) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .ownerRequest(user)
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestDto toDto(ItemRequest request){
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(request.getItems() != null ?
                        request.getItems().stream().map(ItemMap::mapToDto).toList() :
                        List.of())
                .created(request.getCreated())
                .build();
    }
}
