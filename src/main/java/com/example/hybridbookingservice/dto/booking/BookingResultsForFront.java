package com.example.hybridbookingservice.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingResultsForFront {
    private List<BookingResultWithDoctor> upcoming;
    private List<BookingResultWithDoctor> past;
}
