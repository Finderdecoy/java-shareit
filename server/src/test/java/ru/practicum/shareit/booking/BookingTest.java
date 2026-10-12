package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.ShareItTests;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты Бронирования")
public class BookingTest extends ShareItTests {
    private static String URL = "/bookings";
    private Map<Data, Object> dataMap;

    enum Data {
        OwnerId, BookerId, ItemId, Booking
    }

    private Map<Data, Object> prepareBooking() throws Exception {
        Long ownerId = getIdFromObject(createUser(user1));
        Long idUser2 = getIdFromObject(createUser(user2));
        Long idItem = getIdFromObject(createItem(item, ownerId));
        item.setId(idItem);

        BookingDtoCreate booking = BookingDtoCreate.builder()
                .itemId(idItem)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        return Map.of(Data.OwnerId, ownerId,
                Data.BookerId, idUser2,
                Data.ItemId, idItem,
                Data.Booking, booking);
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        dataMap = prepareBooking();
    }

    @Test
    public void createBookingsValidData() throws Exception {

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_HEADER, dataMap.get(Data.BookerId))
                .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking)))
        ).andExpect(status().isOk());
    }

    @Test
    public void createBookingWhenDateIsBusy() throws Exception {

        BookingDtoCreate bookingTest = (BookingDtoCreate) dataMap.get(Data.Booking);

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_HEADER, dataMap.get(Data.BookerId))
                .content(objectMapper.writeValueAsString(bookingTest))
        );

        long idUser3 = getIdFromObject(createUser(user3));

        bookingTest.setStart(LocalDateTime.now().plusHours(12));
        bookingTest.setEnd(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, idUser3)
                        .content(objectMapper.writeValueAsString(bookingTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void selfBooking() throws Exception {
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_HEADER, dataMap.get(Data.OwnerId))
                .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking)))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void approveBookingByOwner() throws Exception {
        ResultActions result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAITING"));

        long bookingId = getIdFromObject(result);

        mockMvc.perform(patch(URL + "/" + bookingId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.OwnerId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void getBookingByValidIdAndValidUser() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(result);

        mockMvc.perform(get(URL + "/" + bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    public void getBookingByIdInValidAndValidUser() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(result);

        mockMvc.perform(get(URL + "/" + bookingId + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBookingByIdValidAndInValidUser() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(result);
        long idUser = (long) dataMap.get(Data.BookerId);
        mockMvc.perform(get(URL + "/" + bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, idUser + 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testSearchBookingRightStatus() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(result);

        mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .param("state", "all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").exists())
                .andExpect(jsonPath("$[0].id").value(bookingId))
                .andExpect(jsonPath("$[0].status").value("WAITING"));

    }

    @Test
    public void testSearchBookingRightStatusByOwner() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long bookingId = getIdFromObject(result);

        mockMvc.perform(get(URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.OwnerId))
                        .param("state", "all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").exists())
                .andExpect(jsonPath("$[0].id").value(bookingId))
                .andExpect(jsonPath("$[0].status").value("WAITING"));

    }

    @Test
    public void testSearchBookingNotRightStatus() throws Exception {
        testFindByStatusUser("Wrong").andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchBookingREJECTEDStatus() throws Exception {
        testFindByStatusUser("REJECTED")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testSearchBookingWithOtherStatuses() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long idBooking = getIdFromObject(result);

        List<String> statuses = List.of("CURRENT", "PAST", "REJECTED");

        for (String status : statuses) {
            testFindByStatusUser(status)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        testFindByStatusUser("FUTURE")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").exists())
                .andExpect(jsonPath("$[0].id").value(idBooking));
    }

    @Test
    public void testSearchBookingWithOtherStatusesForOwner() throws Exception {
        var result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, dataMap.get(Data.BookerId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataMap.get(Data.Booking))))
                .andExpect(status().isOk());

        long idBooking = getIdFromObject(result);

        List<String> statuses = List.of("CURRENT", "PAST", "REJECTED");

        for (String status : statuses) {
            testFindByStatusOwner(status)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        testFindByStatusOwner("FUTURE")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").exists())
                .andExpect(jsonPath("$[0].id").value(idBooking));
    }

    private ResultActions testFindByStatusUser(String status) throws Exception {
        return mockMvc.perform(get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_HEADER, dataMap.get(Data.BookerId))
                .param("state", status)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions testFindByStatusOwner(String status) throws Exception {
        return mockMvc.perform(get(URL + "/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_HEADER, dataMap.get(Data.OwnerId))
                .param("state", status)
                .accept(MediaType.APPLICATION_JSON));
    }
}

