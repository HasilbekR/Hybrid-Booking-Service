package com.example.hybridbookingservice.dto.queue;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsForQueue {
    private UUID id;
    private String name;
}
