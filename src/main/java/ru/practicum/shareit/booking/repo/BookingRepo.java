package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    //запросы арендатора
    List<Booking> findByBookerId(Long userId);

    List<Booking> findByBookingStartDateBeforeAndBookingEndDateAfterAndBooker(LocalDate dateStart, LocalDate dateEnd, User booker);

    List<Booking> findByBookingEndDateBeforeAndBooker(LocalDate dateNow, User booker);

    List<Booking> findByBookingStartDateAfterAndBooker(LocalDate dateNow, User booker);

    List<Booking> findByStatusAndBooker(BookingStatus bookingStatus, User booker);

    //запросы владельца
    List<Booking> findByItemBookingOwnerId(Long userId);

    List<Booking> findByItemBookingIdInAndStatus(List<Long> idItems, BookingStatus status);

    List<Booking> findByBookingStartDateBeforeAndBookingEndDateAfterAndItemBookingOwnerId(LocalDate nowStart, LocalDate nowEnd, Long owner);

    List<Booking> findByStatusAndItemBookingOwnerId(BookingStatus bookingStatus, Long owner);

    List<Booking> findByBookingStartDateAfterAndItemBookingOwnerId(LocalDate now, Long owner);

    List<Booking> findByBookingEndDateBeforeAndItemBookingOwnerId(LocalDate now, Long owner);

    //запрос на проверку арендатора для добовления коментария
    boolean existsByItemBookingIdAndBookerIdAndStatusAndBookingEndDateBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime now);

    //запрос на проверку пересечения дат
    boolean existsByItemBookingIdAndBookingStartDateLessThanAndBookingEndDateGreaterThan(
            Long itemId,
            LocalDateTime end,
            LocalDateTime start
    );

}
