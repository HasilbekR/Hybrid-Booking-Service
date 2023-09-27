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

    @Query("SELECT MAX(q.queueNumber) FROM queues q")
    Long findMaxQueueNumber();

    @Query("SELECT q FROM queues q WHERE q.userId = :id AND q.queueEntityStatus IS NULL")
    Optional<QueueEntity> getActiveQueue(@Param("id") UUID userId);

    @Modifying
    @Query("UPDATE queues q SET q.queueEntityStatus = :status WHERE q.id = :id")
    void updateQueueEntityStatusById(@Param("status") QueueEntityStatus status, @Param("id") UUID id);

    @Query("SELECT COALESCE(MAX(q.queueNumber), 0) FROM queues q WHERE q.queueDate = :date AND q.doctorId = :doctorId")
    Optional<Long> findMaxQueueNumberByQueueDateAndDoctorId(@Param("date") LocalDate date, @Param("doctorId") UUID doctorId);

    List<QueueEntity> findByDoctorIdAndQueueEntityStatus(UUID doctorId, QueueEntityStatus status);

    Optional<QueueEntity> findByUserIdAndQueueEntityStatusIsNull(UUID userId);

    @Query("SELECT COUNT(*) FROM queues WHERE doctorId = :doctorId AND queueEntityStatus = :queueEntityStatus")
    Long countDoctorQueuesByStatus(@Param("doctorId") UUID doctorId, @Param("queueEntityStatus") QueueEntityStatus queueEntityStatus);


    // Define a method to delete queues by date and doctor ID
    void deleteByQueueDateAndDoctorId(LocalDate queueDate, UUID doctorId);

    // Define a method to check if queues exist by date and doctor ID
    boolean existsByQueueDateAndDoctorId(LocalDate queueDate, UUID doctorId);





}
