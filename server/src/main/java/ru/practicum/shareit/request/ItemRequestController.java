package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService service;

    @GetMapping
    public List<ItemRequestDto> getMyRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getMyRequests(userId);
    }

    @PostMapping
    public ItemRequestDto saveRequest(@RequestBody ItemRequestOnCreate dto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.save(userId, dto);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return service.getAllRequests();
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@PathVariable Long requestId) {
        return service.getRequestById(requestId);
    }
}
