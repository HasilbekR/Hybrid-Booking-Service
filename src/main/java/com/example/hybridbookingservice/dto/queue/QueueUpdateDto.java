package com.example.hybridbookingservice.dto.queue;

import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
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

public class QueueUpdateDto {

    @NotNull(message = "userId must not be null")
    private UUID userId;

    @NotNull(message = "doctorId must not be null")
    private UUID doctorId;

    @NotNull(message = "Queue status must not be null")
    private QueueEntityStatus queueEntityStatus;
}
