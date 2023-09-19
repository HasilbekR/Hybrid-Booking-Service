package com.example.hybridbookingservice.entity.booking;

import com.example.hybridbookingservice.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.UUID;

@Entity(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingEntity extends BaseEntity {
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @OneToOne
    private TimeSlot timeSlot;
}
