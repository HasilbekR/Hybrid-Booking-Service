package com.example.hybridbookingservice.dto.booking;

import com.example.hybridbookingservice.entity.booking.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Data
@Builder
public class BookingResultWithDoctor {
    private UUID bookingId;
    private UUID doctorId;
    private BookingStatus status;
    private LocalDate bookingDay;
    private LocalTime bookingTime;
    private String weekDay;
    private String doctorName;
}
