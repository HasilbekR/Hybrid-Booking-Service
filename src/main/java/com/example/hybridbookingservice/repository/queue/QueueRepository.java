package com.example.hybridbookingservice.repository.queue;

import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface QueueRepository extends JpaRepository<QueueEntity, UUID> {

    @Modifying
    @Query("UPDATE queues q SET q.queueEntityStatus = :status WHERE q.id = :id")
    void updateQueueEntityStatusById(@Param("status") QueueEntityStatus status, @Param("id") UUID id);

    @Query("SELECT COALESCE(MAX(q.queueNumber), 0) FROM queues q WHERE q.queueDate = :date AND q.doctorId = :doctorId")
    Long findMaxQueueNumberByQueueDateAndDoctorId(@Param("date") LocalDate date, @Param("doctorId") UUID doctorId);

    List<QueueEntity> findByDoctorIdAndQueueEntityStatus(UUID doctorId, QueueEntityStatus status);

    Optional<QueueEntity> findByUserIdAndQueueEntityStatusIsNull(UUID userId);
    @Query(value = "select count(q) from queues q where q.doctorId = :doctorId and q.queueEntityStatus = :queueEntityStatus")
    Long countDoctorQueuesByBookingStatus(@Param("doctorId") UUID doctorId, @Param("queueEntityStatus") QueueEntityStatus queueEntityStatus);

    @Query(value = "select q from queues q where q.doctorId = :doctorId and q.queueEntityStatus = :queueEntityStatus")
    List<QueueEntity> getQueuesByDoctorIdAndBookingStatus(@Param("doctorId") UUID doctorId, @Param("queueEntityStatus") QueueEntityStatus queueEntityStatus);
}
