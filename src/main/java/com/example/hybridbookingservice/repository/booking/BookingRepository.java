package com.example.hybridbookingservice.repository.booking;

import com.example.hybridbookingservice.entity.booking.BookingEntity;
import com.example.hybridbookingservice.entity.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    @Query(value = "select b from bookings b where b.userId = ?1 and (b.status = ?2 or b.status = ?3) order by b.createdDate desc ")
    List<BookingEntity> getUserPastBookings(UUID userId, BookingStatus scheduled, BookingStatus declined);
    @Query(value = "select b from bookings b where b.userId = ?1 and (b.status = ?2 or b.status = ?3) order by b.createdDate desc ")
    List<BookingEntity> getUserUpcomingBookings(UUID userId, BookingStatus scheduled, BookingStatus inProgress);
    @Query(value = "select b from bookings b where b.timeSlot.doctorId = ?1")
    List<BookingEntity> getDoctorBookings(UUID doctorId);
    @Query(value = "SELECT SUM(CASE WHEN b.status = ?2 OR b.status = ?3 THEN 1 ELSE 0 END) FROM timeslots t INNER JOIN bookings b ON t.id = b.timeSlot.id WHERE t.doctorId = ?1")
    Optional<Long> countDoctorBookingsByStatus(UUID doctorId, BookingStatus status1, BookingStatus status2);
    @Query(value = "SELECT b FROM bookings b WHERE b.timeSlot.doctorId = :doctorId AND (b.status = :status1 OR b.status = :status2)")
    Optional<List<BookingEntity>> getDoctorBookingsByStatus(@Param("doctorId") UUID doctorId, @Param("status1") BookingStatus status1, @Param("status2") BookingStatus status2);
}
