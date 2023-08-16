package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.DoctorAvailability;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.booking.BookingService;
import com.example.hybridbookingservice.service.booking.TimeSlotService;
import com.example.hybridbookingservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hybrid-booking/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;
    private final TimeSlotService timeSlotService;
    private final UserService userService;

    @GetMapping("/get-doctor-available-time")
    public List<TimeSlot> getAvailableTime(
            @RequestBody TimeSlotRequestDto timeSlotRequestDto
            ) {
        return bookingService.getAvailableTimeSlots(timeSlotRequestDto);
    }

    @GetMapping("/{userId}/get-user-bookings")
    public List<BookingEntity> getUserBookings(
            @PathVariable UUID userId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String email = userService.findUserEmailById(userId);

        if (isAdmin || authentication.getName().equals(email)) {
            return bookingService.getUserBookings(userId);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/{doctorId}/get-doctor-bookings")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public List<BookingEntity> getDoctorBookings(
            @PathVariable UUID doctorId
    ) {
        return bookingService.getDoctorBookings(doctorId);
    }

    @PostMapping("/save")
    public BookingEntity save(
            @Valid @RequestBody BookingDto bookingDto,
            Principal principal,
            BindingResult bindingResult
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return bookingService.save(bookingDto, principal);
    }

    @PutMapping("/update")
    public BookingEntity update(
            @Valid @RequestBody BookingUpdateDto bookingUpdateDto,
            BindingResult bindingResult,
            Principal principal
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return bookingService.update(bookingUpdateDto, principal);
    }

    @DeleteMapping("/delete")
    public String delete(
            @RequestBody BookingUpdateDto bookingUpdateDto,
            Principal principal
    ) {
        bookingService.delete(bookingUpdateDto, principal);
        return "Successfully deleted";
    }
    @PostMapping("/create-time-slots")
    public String  createTimeSlots(
            @RequestBody DoctorAvailability doctorAvailability,
            @RequestParam(defaultValue = "30") Integer slotDuration
            ){
        timeSlotService.createTimeSlots(doctorAvailability,Duration.of(slotDuration, ChronoUnit.MINUTES));
        return "successfully created";
    }
}
