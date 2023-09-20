package com.example.hybridbookingservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserRequestDto {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String userState;
    private String userReservationState;
}
