package com.example.hybridbookingservice.dto.queue;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class QueueResultForFront {
    private String doctorName;
    private String userName;
    private String queueCreatedDate;
    private String queueCreatedTime;
    private String status;
    private String roomNumber;
    private String specialty;
    private String address;
}
