package com.example.hybridbookingservice.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotRequestDto {
    private LocalDate bookingDay;
    private UUID doctorId;
}
