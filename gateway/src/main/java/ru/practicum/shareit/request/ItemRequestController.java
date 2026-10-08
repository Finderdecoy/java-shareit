package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getMyRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getMyRequests(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveRequest(@Valid @RequestBody ItemRequestOnCreate dto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.save(userId, dto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId) {
        return requestClient.getRequestById(requestId);
    }
}
