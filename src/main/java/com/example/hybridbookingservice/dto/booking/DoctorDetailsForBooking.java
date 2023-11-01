package com.example.hybridbookingservice.dto.booking;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDetailsForBooking {
    private UUID id;
    private String fullName;
    private String roomNumber;
    private String specialty;
    private String gender;
    private UUID hospitalId;
}
