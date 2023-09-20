package com.example.hybridbookingservice.service.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.repository.booking.BookingRepository;
import com.example.hybridbookingservice.repository.booking.TimeSlotRepository;
import com.example.hybridbookingservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveBooking() {
        BookingDto bookingDto = new BookingDto();
        Principal principal = mock(Principal.class);
        UUID userId = UUID.randomUUID();
        when(userService.findUserIdByEmail(anyString())).thenReturn(userId);

        TimeSlot timeSlot = new TimeSlot();
        UUID timeSlotId = UUID.randomUUID();
        when(timeSlotRepository.findById(eq(timeSlotId))).thenReturn(Optional.of(timeSlot)); // Use eq() for UUID comparison


        bookingService.save(bookingDto, principal);

        verify(timeSlotRepository).save(any(TimeSlot.class));
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    @Test
    public void testUpdateBooking() {
        BookingUpdateDto bookingUpdateDto = new BookingUpdateDto();
        Principal principal = mock(Principal.class);
        UUID userId = UUID.randomUUID();
        when(userService.findUserIdByEmail(anyString())).thenReturn(userId);

        BookingEntity booking = new BookingEntity();
        TimeSlot timeSlot = new TimeSlot();
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));
        when(timeSlotRepository.getAvailableTimeSlot(any(LocalDate.class), any(LocalTime.class), any(UUID.class))).thenReturn(Optional.of(timeSlot));

        bookingService.update(bookingUpdateDto, principal);

        verify(timeSlotRepository, times(2)).save(any(TimeSlot.class));
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    @Test
    public void testDeleteBooking() {
        BookingUpdateDto bookingUpdateDto = new BookingUpdateDto();
        Principal principal = mock(Principal.class);
        UUID userId = UUID.randomUUID();
        when(userService.findUserIdByEmail(anyString())).thenReturn(userId);

        BookingEntity booking = new BookingEntity();
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

        bookingService.delete(bookingUpdateDto, principal);


      verify(timeSlotRepository).save(any(TimeSlot.class));
        verify(bookingRepository).delete(any(BookingEntity.class));
    }

    @Test
    public void testGetUserBookings() {
        UUID userId = UUID.randomUUID();
        List<BookingEntity> mockBookings = new ArrayList<>();
        when(bookingRepository.getBookingEntityByUserId(any(UUID.class))).thenReturn(mockBookings);

        List<BookingEntity> result = bookingService.getUserBookings(userId);

        assertEquals(mockBookings, result);
    }

    @Test
    public void testGetDoctorBookings() {
        UUID doctorId = UUID.randomUUID();
        List<BookingEntity> mockBookings = new ArrayList<>();
        when(bookingRepository.getDoctorBookings(any(UUID.class))).thenReturn(mockBookings);

        List<BookingEntity> result = bookingService.getDoctorBookings(doctorId);

        assertEquals(mockBookings, result);
    }

    @Test
    public void testGetAvailableTimeSlots() {

        TimeSlotRequestDto timeSlotRequestDto = new TimeSlotRequestDto(); // Initialize with required data
        List<TimeSlot> mockTimeSlots = new ArrayList<>();
        when(timeSlotRepository.getDoctorAvailableTimeSlotForTheDay(any(LocalDate.class), any(UUID.class))).thenReturn(mockTimeSlots);

        List<TimeSlot> result = bookingService.getAvailableTimeSlots(timeSlotRequestDto);

        assertEquals(mockTimeSlots, result);
    }
}
