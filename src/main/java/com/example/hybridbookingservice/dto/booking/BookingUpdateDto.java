package com.example.hybridbookingservice.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateDto {
    private UUID bookingId;
    private LocalDate bookingDay;
    private LocalTime bookingTime;
}
