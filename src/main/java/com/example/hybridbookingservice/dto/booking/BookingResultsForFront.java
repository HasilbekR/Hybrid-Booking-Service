package com.example.hybridbookingservice.dto.booking;

import com.example.hybridbookingservice.entity.booking.BookingEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingResultsForFront {
    private List<BookingEntity> upcoming;
    private List<BookingEntity> past;
}
