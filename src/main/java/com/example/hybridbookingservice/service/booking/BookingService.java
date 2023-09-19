package com.example.hybridbookingservice.service.booking;

import com.example.hybridbookingservice.dto.booking.BookingDto;
import com.example.hybridbookingservice.dto.booking.BookingUpdateDto;
import com.example.hybridbookingservice.dto.booking.TimeSlotRequestDto;
import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.BookingStatus;
import com.example.hybridbookingservice.entity.booking.TimeSlot;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.repository.booking.BookingRepository;
import com.example.hybridbookingservice.repository.booking.TimeSlotRepository;
import com.example.hybridbookingservice.repository.queue.QueueRepository;
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
    private final QueueRepository queueRepository;
    private final UserService userService;

    public BookingEntity save(BookingDto bookingDto, Principal principal) {
        String name = principal.getName();
        UUID userId = userService.findUserIdByEmail(name);
        TimeSlot timeSlot = timeSlotRepository.findById(bookingDto.getTimeSlotId()).orElseThrow(() -> new DataNotFoundException("Not available time slot"));
        BookingEntity booking = BookingEntity.builder()
                .userId(userId)
                .timeSlot(timeSlot)
                .bookingStatus(BookingStatus.ACTIVE)
                .build();
        timeSlot.setAvailability(false);
        timeSlot.setUpdatedDate(LocalDateTime.now());
        timeSlotRepository.save(timeSlot);
        return bookingRepository.save(booking);
    }

    public BookingEntity update(BookingUpdateDto bookingUpdateDto, Principal principal) {
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
        return bookingRepository.save(booking);
    }

    public void delete(BookingUpdateDto bookingUpdateDto, Principal principal) {
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
    }

    public List<BookingEntity> getUserBookings(UUID userId) {
        return bookingRepository.getBookingEntityByUserId(userId);
    }

    public List<BookingEntity> getDoctorBookings(UUID doctorId) {
        return bookingRepository.getDoctorBookings(doctorId);
    }

    public List<TimeSlot> getAvailableTimeSlots(TimeSlotRequestDto timeSlotRequestDto) {
        return timeSlotRepository.getDoctorAvailableTimeSlotForTheDay(timeSlotRequestDto.getBookingDay(), timeSlotRequestDto.getDoctorId());
    }

    public Long countDoctorActiveBookings(UUID doctorId) {
        return bookingRepository.countDoctorBookingEntitiesByBookingStatus(doctorId, BookingStatus.ACTIVE);
    }

    public Long countDoctorCompletedBookings(UUID doctorId) {
        return bookingRepository.countDoctorBookingEntitiesByBookingStatus(doctorId, BookingStatus.COMPLETED);
    }

    public List<BookingEntity> getBookingsByDoctorIdAndBookingStatusActive(UUID doctorId) {
        return bookingRepository.getBookingsByDoctorIdAndBookingStatus(doctorId, BookingStatus.ACTIVE);
    }

    public List<BookingEntity> getBookingsByDoctorIdAndBookingStatusComplete(UUID doctorId) {
        return bookingRepository.getBookingsByDoctorIdAndBookingStatus(doctorId, BookingStatus.COMPLETED);
    }
}
