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
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final UserService userService;
    public StandardResponse<String> createTimeSlots(DoctorAvailability doctorAvailability, Duration slotDuration, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult.getAllErrors());
        }
        List<TimeSlot> existingTimeSlots = timeSlotRepository.getDoctorTimeSlotsForTheDay(doctorAvailability.getDay(), doctorAvailability.getDoctorId());
        List<TimeSlot> newTimeSlots = new LinkedList<>();

        LocalTime currentTime = doctorAvailability.getStartingTime();
        String userEmailById = userService.findUserEmailById(doctorAvailability.getDoctorId());
        if (userEmailById == null) throw new DataNotFoundException("Doctor not found");

        while (currentTime.isBefore(doctorAvailability.getEndingTime())) {
            LocalTime finalCurrentTime = currentTime;
            boolean timeSlotExists = existingTimeSlots.stream()
                    .anyMatch(slot -> slot.getBookingDay().equals(doctorAvailability.getDay()) && slot.getBookingTime().equals(finalCurrentTime));
            if(!timeSlotExists) {
                TimeSlot timeSlot = new TimeSlot(doctorAvailability.getDay(), currentTime, true, doctorAvailability.getDoctorId());
                newTimeSlots.add(timeSlot);
            }
            currentTime = currentTime.plus(slotDuration);
        }
        timeSlotRepository.saveAll(newTimeSlots);
        return StandardResponse.<String>builder().status(Status.SUCCESS).message("Time slots successfully created for "+doctorAvailability.getDay())
                .build();
    }
}
