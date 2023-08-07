package com.example.hybridbookingservice.repository.queue;

import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface QueueRepository extends JpaRepository<QueueEntity, UUID> {

    @Query("select q from queues q where q.userId = :id and q.queueEntityStatus = null")
    Optional<QueueEntity> getActiveQueue(@Param("id")UUID userId);

    @Modifying
    @Transactional
    @Query("update queues set queueEntityStatus = :status where id = :id")
    void update(@Param("status") QueueEntityStatus status, @Param("id") UUID id);

    @Query("SELECT COALESCE(MAX(q.queueNumber), 0) FROM queues q WHERE q.queueDate = :date and q.doctorId = :doctorId")
    Long findMaxQueueNumberByDateAndByDoctorId(LocalDate date, UUID doctorId);
}
