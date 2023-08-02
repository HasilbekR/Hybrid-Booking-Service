package com.example.hybridbookingservice.entity.booking;

import com.example.hybridbookingservice.entity.BaseEntity;
import jakarta.persistence.Entity;
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
    @OneToOne
    private TimeSlot timeSlot;
}
