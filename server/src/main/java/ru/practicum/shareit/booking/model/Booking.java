package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item itemBooking;
    @Column(name = "date_start_booking")
    private LocalDateTime bookingStartDate;
    @Column(name = "date_end_booking")
    private LocalDateTime bookingEndDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_booking", nullable = false)
    private User booker;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
}
