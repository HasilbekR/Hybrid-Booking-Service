package com.example.hybridbookingservice.dto.request;

import jakarta.persistence.metamodel.ListAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DoctorRequestDto {
    private String fullName;
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
