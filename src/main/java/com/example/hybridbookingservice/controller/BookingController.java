package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.DoctorAvailability;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.dto.request.UserDetailsRequestDto;
import com.example.hybridbookingservice.dto.request.UserRequestDto;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.booking.BookingService;
import com.example.hybridbookingservice.service.booking.TimeSlotService;
import com.example.hybridbookingservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/get-doctor-available-time")
    public ResponseEntity<List<TimeSlot>> getAvailableTime(
            @RequestBody TimeSlotRequestDto timeSlotRequestDto
            ) {
        return ResponseEntity.ok(bookingService.getAvailableTimeSlots(timeSlotRequestDto));
    }

    @GetMapping("/{userId}/get-user-bookings")
    public ResponseEntity<List<BookingEntity>> getUserBookings(
            @PathVariable UUID userId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String email = userService.findUserEmailById(userId);

        if (isAdmin || authentication.getName().equals(email)) {
            return ResponseEntity.ok(bookingService.getUserBookings(userId));
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/{doctorId}/get-doctor-bookings")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingEntity>> getDoctorBookings(
            @PathVariable UUID doctorId
    ) {
        return ResponseEntity.ok(bookingService.getDoctorBookings(doctorId));
    }

    @PostMapping("/save")
    public ResponseEntity<BookingEntity> save(
            @Valid @RequestBody BookingDto bookingDto,
            Principal principal,
            BindingResult bindingResult
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return ResponseEntity.ok(bookingService.save(bookingDto, principal));
    }

    @PostMapping("/update")
    public ResponseEntity<BookingEntity> update(
            @Valid @RequestBody BookingUpdateDto bookingUpdateDto,
            BindingResult bindingResult,
            Principal principal
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return ResponseEntity.ok(bookingService.update(bookingUpdateDto, principal));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestBody BookingUpdateDto bookingUpdateDto,
            Principal principal
    ) {
        bookingService.delete(bookingUpdateDto, principal);
        return ResponseEntity.ok("Successfully deleted");
    }
    @PostMapping("/create-time-slots")
    public ResponseEntity<String> createTimeSlots(
            @RequestBody DoctorAvailability doctorAvailability,
            @RequestParam(defaultValue = "30") Integer slotDuration
            ){
        timeSlotService.createTimeSlots(doctorAvailability,Duration.of(slotDuration, ChronoUnit.MINUTES));
        return ResponseEntity.ok("successfully created");
    }

    @PostMapping("/count-bookings-by-doctorId-and-bookingStatus-active")
    public ResponseEntity<Long> getCountBookingsByDoctorIdAndBookingStatusActive(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(bookingService.countDoctorActiveBookings(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/count-bookings-by-doctorId-and-bookingStatus-complete")
    public ResponseEntity<Long> getCountBookingsByDoctorIdAndBookingStatusComplete(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(bookingService.countDoctorCompletedBookings(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/get-bookings-by-doctorId-and-bookingStatus-active")
    public ResponseEntity<List<BookingEntity>> getBookingsByDoctorIdAndBookingStatusActive(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(bookingService.getBookingsByDoctorIdAndBookingStatusActive(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/get-bookings-by-doctorId-and-bookingStatus-complete")
    public ResponseEntity<List<BookingEntity>> getBookingsByDoctorIdAndBookingStatusComplete(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(bookingService.getBookingsByDoctorIdAndBookingStatusComplete(UUID.fromString(userDetailsRequestDto.getSource())));
    }


}
