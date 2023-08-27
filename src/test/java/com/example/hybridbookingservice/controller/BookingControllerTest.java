package com.example.hybridbookingservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.DoctorAvailability;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.booking.BookingService;
import com.example.hybridbookingservice.service.booking.TimeSlotService;
import com.example.hybridbookingservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @Mock
    private TimeSlotService timeSlotService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableTime() {
        TimeSlotRequestDto timeSlotRequestDto = new TimeSlotRequestDto(); // Initialize with required data
        List<TimeSlot> mockTimeSlots = new ArrayList<>();
        when(bookingService.getAvailableTimeSlots(eq(timeSlotRequestDto))).thenReturn(mockTimeSlots);

        ResponseEntity<List<TimeSlot>> response = bookingController.getAvailableTime(timeSlotRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTimeSlots, response.getBody());
        verify(bookingService, times(1)).getAvailableTimeSlots(eq(timeSlotRequestDto));
    }

    @Test
    public void testGetUserBookings() {
        UUID userId = UUID.randomUUID();
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userService.findUserEmailById(eq(userId))).thenReturn("test@example.com");
        List<BookingEntity> mockBookings = new ArrayList<>();
        when(bookingService.getUserBookings(eq(userId))).thenReturn(mockBookings);

        ResponseEntity<List<BookingEntity>> response = bookingController.getUserBookings(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBookings, response.getBody());
        verify(bookingService, times(1)).getUserBookings(eq(userId));
    }

    @Test
    public void testGetDoctorBookings() {
        UUID doctorId = UUID.randomUUID();
        List<BookingEntity> mockBookings = new ArrayList<>();
        when(bookingService.getDoctorBookings(eq(doctorId))).thenReturn(mockBookings);

        ResponseEntity<List<BookingEntity>> response = bookingController.getDoctorBookings(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBookings, response.getBody());
        verify(bookingService, times(1)).getDoctorBookings(eq(doctorId));
    }

    @Test
    public void testSaveBooking() throws RequestValidationException {
        BookingDto bookingDto = new BookingDto();
        Principal principal = mock(Principal.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<BookingEntity> response = bookingController.save(bookingDto, principal, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService, times(1)).save(eq(bookingDto), eq(principal));
    }

    @Test
    public void testUpdateBooking() throws RequestValidationException {
        BookingUpdateDto bookingUpdateDto = new BookingUpdateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        Principal principal = mock(Principal.class);

        ResponseEntity<BookingEntity> response = bookingController.update(bookingUpdateDto, bindingResult, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService, times(1)).update(eq(bookingUpdateDto), eq(principal));
    }

    @Test
    public void testDeleteBooking() {
        BookingUpdateDto bookingUpdateDto = new BookingUpdateDto();
        Principal principal = mock(Principal.class);

        ResponseEntity<String> response = bookingController.delete(bookingUpdateDto, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService, times(1)).delete(eq(bookingUpdateDto), eq(principal));
    }

    @Test
    public void testCreateTimeSlots() {
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        Integer slotDuration = 30;
        doNothing().when(timeSlotService).createTimeSlots(eq(doctorAvailability), eq(Duration.of(slotDuration, ChronoUnit.MINUTES)));

        ResponseEntity<String> response = bookingController.createTimeSlots(doctorAvailability, slotDuration);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("successfully created", response.getBody());
        verify(timeSlotService, times(1)).createTimeSlots(eq(doctorAvailability), eq(Duration.of(slotDuration, ChronoUnit.MINUTES)));
    }
}
