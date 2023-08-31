package com.example.hybridbookingservice.service.booking;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.dto.response.StandardResponse;
import com.example.hybridbookingservice.dto.response.Status;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.repository.booking.BookingRepository;
import com.example.hybridbookingservice.repository.booking.TimeSlotRepository;
import com.example.hybridbookingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    public StandardResponse<BookingEntity> save(BookingDto bookingDto, Principal principal) {
        String name = principal.getName();
        UUID userId = userService.findUserIdByEmail(name);
        TimeSlot timeSlot = timeSlotRepository.findById(bookingDto.getTimeSlotId()).orElseThrow(() -> new DataNotFoundException("Not available time slot"));
        BookingEntity booking = BookingEntity.builder()
                .userId(userId)
                .timeSlot(timeSlot)
                .build();
        timeSlot.setAvailability(false);
        timeSlot.setUpdatedDate(LocalDateTime.now());
        timeSlotRepository.save(timeSlot);
        return StandardResponse.<BookingEntity>builder().status(Status.SUCCESS).message("Booking successfully saved")
                .data(bookingRepository.save(booking)).build();
    }

    public StandardResponse<BookingEntity> update(BookingUpdateDto bookingUpdateDto, Principal principal) {
        BookingEntity booking = bookingRepository.findById(bookingUpdateDto.getBookingId()).orElseThrow(() -> new DataNotFoundException("Booking not found"));
        UUID userIdByEmail = userService.findUserIdByEmail(principal.getName());
        if (!booking.getUserId().equals(userIdByEmail)) {
            throw new AccessDeniedException("You do not have access to update this booking");
        }
        TimeSlot availableTimeSlot = timeSlotRepository.getAvailableTimeSlot(bookingUpdateDto.getBookingDay(), bookingUpdateDto.getBookingTime(), booking.getTimeSlot().getDoctorId())
                .orElseThrow(() -> new AccessDeniedException("Unavailable time slot"));
        TimeSlot timeSlot = booking.getTimeSlot();
        timeSlot.setAvailability(true);
        timeSlot.setUpdatedDate(LocalDateTime.now());
        timeSlotRepository.save(timeSlot);

        availableTimeSlot.setAvailability(false);
        availableTimeSlot.setUpdatedDate(LocalDateTime.now());
        timeSlotRepository.save(availableTimeSlot);

        booking.setTimeSlot(availableTimeSlot);
        return StandardResponse.<BookingEntity>builder().status(Status.SUCCESS).message("Booking successfully updated")
                .data(bookingRepository.save(booking)).build();
    }

    public StandardResponse<String> delete(BookingUpdateDto bookingUpdateDto, Principal principal) {
        BookingEntity booking = bookingRepository.findById(bookingUpdateDto.getBookingId()).orElseThrow(() -> new DataNotFoundException("Booking not found"));
        UUID userIdByEmail = userService.findUserIdByEmail(principal.getName());
        if (!booking.getUserId().equals(userIdByEmail)) {
            throw new AccessDeniedException("You do not have access to delete this booking");
        }
        TimeSlot timeSlot = booking.getTimeSlot();
        timeSlot.setAvailability(true);
        timeSlot.setUpdatedDate(LocalDateTime.now());
        timeSlotRepository.save(timeSlot);
        bookingRepository.delete(booking);
        return StandardResponse.<String>builder().status(Status.SUCCESS).message("Booking successfully deleted").build();
    }

    public StandardResponse<List<BookingEntity>> getUserBookings(UUID userId) {
        return StandardResponse.<List<BookingEntity>>builder().status(Status.SUCCESS).message("User's bookings")
                .data(bookingRepository.getBookingEntityByUserIdOrderByCreatedDateDesc(userId)).build();
    }

    public StandardResponse<List<BookingEntity>> getDoctorBookings(UUID doctorId) {
        return StandardResponse.<List<BookingEntity>>builder().status(Status.SUCCESS)
                .message("Doctor's booking list")
                .data(bookingRepository.getDoctorBookings(doctorId)).build();
    }

    public StandardResponse<List<TimeSlot>> getAvailableTimeSlots(TimeSlotRequestDto timeSlotRequestDto) {
        return StandardResponse.<List<TimeSlot>>builder()
                .status(Status.SUCCESS)
                .message("Doctor's available time slots")
                .data(timeSlotRepository.getDoctorAvailableTimeSlotForTheDay(timeSlotRequestDto.getBookingDay(), timeSlotRequestDto.getDoctorId())).build();
    }

}
