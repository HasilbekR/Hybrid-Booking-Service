package com.example.hybridbookingservice.entity.queue;

import com.example.hybridbookingservice.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;
@Entity(name = "queues")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class QueueEntity extends BaseEntity {
    private UUID userId;

    private UUID doctorId;

    private Long queueNumber;

    @Enumerated(EnumType.STRING)
    private QueueEntityStatus queueEntityStatus;

}
