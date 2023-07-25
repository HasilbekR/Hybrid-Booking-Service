package com.example.hybridbookingservice.dto.queue;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class QueueCreateDto {

    @NotBlank(message = "userId must not be empty")
    private UUID userId;

    @NotBlank(message = "doctorId must not be empty")
    private UUID doctorId;


}
