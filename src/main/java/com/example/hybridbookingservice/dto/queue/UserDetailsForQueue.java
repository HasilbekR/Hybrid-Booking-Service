package com.example.hybridbookingservice.dto.queue;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class UserDetailsForQueue {
    private UUID id;
    private String name;
}
