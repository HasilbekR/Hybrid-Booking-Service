package com.example.hybridbookingservice.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;
@Data
@Builder
public class AvailableTimeSlots {
    private UUID id;
    private LocalTime bookingTime;
}
