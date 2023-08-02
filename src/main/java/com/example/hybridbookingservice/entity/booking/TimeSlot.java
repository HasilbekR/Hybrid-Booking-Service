package com.example.hybridbookingservice.entity.booking;

import com.example.hybridbookingservice.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Entity(name = "timeslots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot extends BaseEntity {
    @Column(columnDefinition = "DATE")
    private LocalDate bookingDay;
    private LocalTime bookingTime;
    private boolean availability;
    private UUID doctorId;
}
