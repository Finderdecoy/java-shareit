package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookingItem(@Validated @RequestBody BookingDtoCreate bookingDto,
                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestParam(name = "approved", required = true) Boolean status,
                                   @PathVariable Long bookingId,
                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.changeStatus(status, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getListBooking(@RequestParam(name = "state", defaultValue = "all") String state,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getListBooking(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getListOwner(@RequestParam(name = "state", defaultValue = "all") String state,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getListOwner(state, userId);
    }
}
