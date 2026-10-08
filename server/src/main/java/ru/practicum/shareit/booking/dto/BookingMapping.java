package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapping {

    public static BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(ItemMap.mapToDto(booking.getItemBooking()))
                .start(booking.getBookingStartDate())
                .end(booking.getBookingEndDate())
                .status(booking.getStatus())
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .build();
    }

    public static Booking mapToBookingOnCreate(BookingDtoCreate dto, Item item, User booker) {
        return Booking.builder()
                .itemBooking(item)
                .bookingStartDate(dto.getStart())
                .bookingEndDate(dto.getEnd())
                .status(BookingStatus.WAITING)
                .booker(booker)
                .build();
    }

}
