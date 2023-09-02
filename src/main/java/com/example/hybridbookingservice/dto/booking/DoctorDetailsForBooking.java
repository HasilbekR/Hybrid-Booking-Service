package com.example.hybridbookingservice.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorDetailsForBooking {
    private UUID id;
    private String fullName;
    private String roomNumber;
    private String specialty;
    private UUID hospitalId;
}
