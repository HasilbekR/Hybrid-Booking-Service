package com.example.hybridbookingservice.dto.queue;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "userId must not be empty")
    private UUID userId;

    @NotNull(message = "doctorId must not be empty")
    private UUID doctorId;


}
