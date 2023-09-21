package com.example.hybridbookingservice.repository.booking;

import com.example.hybridbookingservice.entity.booking.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    @Query(value = "select b from bookings b where b.userId = ?1 and (b.bookingStatus = 'COMPLETED' or b.bookingStatus = 'DECLINED') order by b.createdDate desc ")
    List<BookingEntity> getUserPastBookings(UUID userId);
    @Query(value = "select b from bookings b where b.userId = ?1 and (b.bookingStatus = 'SCHEDULED' or b.bookingStatus = 'IN_PROGRESS') order by b.createdDate desc ")
    List<BookingEntity> getUserUpcomingBookings(UUID userId);
    @Query(value = "select b from bookings b where b.timeSlot.doctorId = ?1")
    List<BookingEntity> getDoctorBookings(UUID doctorId);
}
