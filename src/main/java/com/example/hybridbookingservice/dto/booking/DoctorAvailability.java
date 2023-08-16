package com.example.hybridbookingservice.dto.booking;

import com.example.hybridbookingservice.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DoctorAvailability extends BaseEntity {
    private UUID doctorId;
    private LocalDate day;
    private LocalTime startingTime;
    private LocalTime endingTime;
}

