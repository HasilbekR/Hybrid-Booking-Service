package com.example.hybridbookingservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DoctorRequestDto {
    private UUID doctorId;
    private String fullName;
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
