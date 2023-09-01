package com.example.hybridbookingservice.service.booking;

import com.example.hybridbookingservice.dto.booking.DoctorAvailability;
import com.example.hybridbookingservice.dto.response.StandardResponse;
import com.example.hybridbookingservice.dto.response.Status;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.repository.booking.TimeSlotRepository;
import com.example.hybridbookingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    public StandardResponse<String> createTimeSlots(DoctorAvailability doctorAvailability, Duration slotDuration, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult.getAllErrors());
        }
        LocalTime currentTime = doctorAvailability.getStartingTime();
        while (currentTime.isBefore(doctorAvailability.getEndingTime())) {
            TimeSlot timeSlot = new TimeSlot(doctorAvailability.getDay(), currentTime, true, doctorAvailability.getDoctorId());
            timeSlotRepository.save(timeSlot);
            currentTime = currentTime.plus(slotDuration);
        }
        return StandardResponse.<String>builder().status(Status.SUCCESS).message("Time slots successfully created for "+doctorAvailability.getDay())
                .build();
    }
}
