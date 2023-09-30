package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.booking.*;
import com.example.hybridbookingservice.dto.request.ExchangeDataDto;
import com.example.hybridbookingservice.dto.response.StandardResponse;
import com.example.hybridbookingservice.dto.response.Status;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.booking.BookingService;
import com.example.hybridbookingservice.service.booking.TimeSlotService;
import com.example.hybridbookingservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hybrid-booking")
public class BookingController {
    private final BookingService bookingService;
    private final TimeSlotService timeSlotService;
    private final UserService userService;

    @PostMapping("/get-doctor-available-time")
    public StandardResponse<List<AvailableTimeSlots>> getAvailableTime(
            @RequestBody TimeSlotRequestDto timeSlotRequestDto
    ) {
        return bookingService.getAvailableTimeSlots(timeSlotRequestDto);
    }

    @GetMapping("/get-user-bookings")
    public StandardResponse<BookingResultsForFront> getUserBookings(
            Principal principal
    ) {
        return bookingService.getUserBookings(userService.findUserIdByEmail(principal.getName()));
    }
    @GetMapping("/get-booking")
    public StandardResponse<BookingResultWithDoctor> getBooking(
            @RequestParam UUID bookingId
    ){
        return bookingService.getBooking(bookingId);
    }

    @GetMapping("/{doctorId}/get-doctor-bookings")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<List<BookingEntity>> getDoctorBookings(
            @PathVariable UUID doctorId
    ) {
        return bookingService.getDoctorBookings(doctorId);
    }

    @PostMapping("/save")
    public StandardResponse<BookingEntity> save(
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

    @GetMapping("/cancel")
    public StandardResponse<String> cancel(
            @RequestParam UUID bookingId,
            Principal principal
    ) throws RequestValidationException {
        return bookingService.cancel(bookingId, principal);
    }

    @DeleteMapping("/delete")
    public StandardResponse<String> delete(
            @RequestBody BookingUpdateDto bookingUpdateDto,
            Principal principal
    ) {
        return bookingService.delete(bookingUpdateDto, principal);
    }
    @PostMapping("/create-time-slots")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<String> createTimeSlots(
            @RequestBody DoctorAvailability doctorAvailability,
            @RequestParam(defaultValue = "30") Integer slotDuration,
            BindingResult bindingResult
    ){
        return timeSlotService.createTimeSlots(doctorAvailability,Duration.of(slotDuration, ChronoUnit.MINUTES),bindingResult);
    }
    @PostMapping("/send-working-days-of-doctor")
    public List<LocalDate> getWorkingDaysOfDoctor(
            @RequestBody ExchangeDataDto exchangeDataDto
    ){
        return bookingService.getWorkingDaysOfDoctor(UUID.fromString(exchangeDataDto.getSource()));
    }

    @PostMapping("/count-doctor-bookings-status-active")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<Long> countDoctorBookingsStatusActive(
            @RequestBody ExchangeDataDto exchangeDataDto
    ) {
        Long amountActiveBookings = bookingService.countDoctorBookingsStatusActive(UUID.fromString(exchangeDataDto.getSource()));
        return StandardResponse.<Long>
                builder()
                .status(Status.SUCCESS)
                .message("Doctor active bookings count")
                .data(amountActiveBookings)
                .build();
    }

    @PostMapping("/count-doctor-bookings-status-complete")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<Long> countDoctorBookingsStatusComplete(
            @RequestBody ExchangeDataDto exchangeDataDto
    ) {
        Long amountCompleteBookings = bookingService.countDoctorBookingsStatusComplete(UUID.fromString(exchangeDataDto.getSource()));
        return StandardResponse.<Long>
                        builder()
                .status(Status.SUCCESS)
                .message("Doctor active bookings count")
                .data(amountCompleteBookings)
                .build();
    }

    @PostMapping("/get-doctor-bookings-status-active")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<BookingEntity> getDoctorBookingsStatusActive(
            @RequestBody ExchangeDataDto exchangeDataDto
    ) {
        List<BookingEntity> doctorBookingsStatusActive = bookingService.getDoctorBookingsStatusActive(UUID.fromString(exchangeDataDto.getSource()));
        return StandardResponse.<BookingEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Doctor active bookings")
                .data((BookingEntity) doctorBookingsStatusActive)
                .build();
    }

    @PostMapping("/get-doctor-bookings-status-complete")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public StandardResponse<BookingEntity> getDoctorBookingsStatusComplete(
            @RequestBody ExchangeDataDto exchangeDataDto
    ) {
        List<BookingEntity> doctorBookingsStatusCompleted = bookingService.getDoctorBookingsStatusCompleted(UUID.fromString(exchangeDataDto.getSource()));
        return StandardResponse.<BookingEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Doctor bookings status completed")
                .data((BookingEntity) doctorBookingsStatusCompleted)
                .build();
    }

}
