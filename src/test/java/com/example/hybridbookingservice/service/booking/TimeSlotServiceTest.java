package com.example.hybridbookingservice.service.booking;

import static org.mockito.Mockito.*;

import com.example.hybridbookingservice.dto.booking.DoctorAvailability;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.repository.booking.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TimeSlotServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private TimeSlotService timeSlotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTimeSlots() {
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setDay(LocalDate.now());
        doctorAvailability.setStartingTime(LocalTime.of(9, 0));
        doctorAvailability.setEndingTime(LocalTime.of(17, 0));
        doctorAvailability.setDoctorId(UUID.randomUUID());
        Duration slotDuration = Duration.ofMinutes(30);

        timeSlotService.createTimeSlots(doctorAvailability, slotDuration);

        verify(timeSlotRepository, times(16)).save(any(TimeSlot.class)); // Assuming 8 hours, 30 minutes each
    }
}
