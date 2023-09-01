package com.example.hybridbookingservice.repository.booking;

import com.example.hybridbookingservice.entity.booking.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
    @Query(value = "select t from timeslots t where t.availability = true and t.bookingDay = ?1 and t.doctorId = ?2 and (t.bookingDay <> ?4 or t.bookingTime >= ?3) order by t.bookingTime asc")
    List<TimeSlot> getDoctorAvailableTimeSlotForTheDay(LocalDate localDate, UUID doctorId, LocalTime now, LocalDate today);
    @Query(value = "select t from timeslots t where t.availability = true  and t.bookingDay = ?1 and t.bookingTime = ?2 and t.doctorId = ?3")
    Optional<TimeSlot> getAvailableTimeSlot(LocalDate localDate, LocalTime localTime, UUID doctorId);
    @Query(value = "select distinct t.bookingDay from timeslots t where t.bookingDay >= ?1 and t.doctorId = ?2 order by t.bookingDay asc limit 6")
    List<LocalDate> getWorkingDaysOfDoctor(LocalDate localDate, UUID doctorId);
}
