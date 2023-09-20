package com.example.hybridbookingservice.repository.booking;

import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.BookingStatus;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> getBookingEntityByUserId(UUID userId);
    @Query(value = "select b from bookings b where b.timeSlot.doctorId = ?1")
    List<BookingEntity> getDoctorBookings(UUID doctorId);
    @Query(value = "select count(b) from bookings b where b.timeSlot.doctorId = :doctorId and b.bookingStatus = :bookingStatus")
    Long countDoctorBookingEntitiesByBookingStatus(@Param("doctorId") UUID doctorId, @Param("bookingStatus") BookingStatus bookingStatus);
    @Query(value = "select b from bookings b where b.timeSlot.doctorId = :doctorId and b.bookingStatus = :bookingStatus")
    List<BookingEntity> getBookingsByDoctorIdAndBookingStatus(@Param("doctorId") UUID doctorId, @Param("bookingStatus") BookingStatus bookingStatus);
}
