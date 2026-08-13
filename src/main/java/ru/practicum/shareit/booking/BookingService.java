package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepo;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepo bookingRepo;
    private final ItemRepo itemRepo;
    private final UserService userService;

    public BookingDto createBooking(BookingDtoCreate dto, Long userBooking) {
        Item item = itemRepo.findById(dto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User booker = userService.getUser(userBooking);
        Booking booking = BookingMapping.mapToBookingOnCreate(dto, item, booker);
        if (booking.getItemBooking().getAvailable()) return BookingMapping.mapToDto(bookingRepo.save(booking));
        throw new RuntimeException("Вещь не доступна в аренду");
    }

    public BookingDto changeStatus(Boolean status, Long bookingId, Long userId) {
        Booking booking = getBookingById(bookingId);
        log.info("Id item booking {} , userId {}", booking.getItemBooking().getOwner().getId(), userId);
        if (!booking.getItemBooking().getOwner().getId().equals(userId)) {
            throw new StatusException("Только владелец вещи может изменить статус");
        }
        log.info("Изменение статуса {}, статус в запросе {}", booking, status);
        if (booking.getStatus() == BookingStatus.WAITING) {
            booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
            return BookingMapping.mapToDto(bookingRepo.save(booking));
        }
        throw new StatusException("Стату уже изменнен");
    }

    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = getBookingById(bookingId);
        log.info("Пользователь с id = {}, отправил запрос на просмотр {}", userId, booking);
        if (booking.getBooker().getId().equals(userId) || booking.getItemBooking().getOwner().getId().equals(userId)) {
            log.info("Права доступа совпадают, вывод информации ...");
            return BookingMapping.mapToDto(booking);
        }
        throw new ValidateException("Не правд доступа на просмотрет аренды");
    }

    public List<BookingDto> getListBooking(String state, Long userId) {
        BookingState bState = BookingState.fromString(state);
        User booker = userService.getUser(userId);
        List<Booking> bookings = switch (bState) {
            case ALL -> bookingRepo.findByBookerId(userId);
            case CURRENT ->
                    bookingRepo.findByBookingStartDateBeforeAndBookingEndDateAfterAndBooker(LocalDate.now(), LocalDate.now(), booker);
            case PAST -> bookingRepo.findByBookingEndDateBeforeAndBooker(LocalDate.now(), booker);
            case FUTURE -> bookingRepo.findByBookingStartDateAfterAndBooker(LocalDate.now(), booker);
            case WAITING -> bookingRepo.findByStatusAndBooker(BookingStatus.WAITING, booker);
            case REJECTED -> bookingRepo.findByStatusAndBooker(BookingStatus.REJECTED, booker);
        };
        return bookings.stream().map(BookingMapping::mapToDto).toList();
    }

    public List<BookingDto> getListOwner(String state, Long userId) {
        BookingState bState = BookingState.fromString(state);
        User owner = userService.getUser(userId);
        List<Booking> bookings = switch (bState) {
            case ALL -> bookingRepo.findByItemBookingOwnerId(userId);
            case CURRENT ->
                    bookingRepo.findByBookingStartDateBeforeAndBookingEndDateAfterAndItemBookingOwnerId(LocalDate.now(), LocalDate.now(), owner.getId());
            case PAST -> bookingRepo.findByBookingEndDateBeforeAndItemBookingOwnerId(LocalDate.now(), owner.getId());
            case FUTURE -> bookingRepo.findByBookingStartDateAfterAndItemBookingOwnerId(LocalDate.now(), owner.getId());
            case WAITING -> bookingRepo.findByStatusAndItemBookingOwnerId(BookingStatus.WAITING, owner.getId());
            case REJECTED -> bookingRepo.findByStatusAndItemBookingOwnerId(BookingStatus.REJECTED, owner.getId());
        };
        return bookings.stream().map(BookingMapping::mapToDto).toList();
    }

    private Booking getBookingById(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new NotFoundException("Аренда с таким id не найдена"));
        return booking;
    }
}
