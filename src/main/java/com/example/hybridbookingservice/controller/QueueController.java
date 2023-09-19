package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.dto.request.UserDetailsRequestDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.service.doctor.DoctorService;
import com.example.hybridbookingservice.service.queue.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hybrid-booking/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;
    private final DoctorService doctorService;
    @PostMapping("/add-queue")
//    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<QueueEntity> addQueue(

            @Valid @RequestBody QueueCreateDto queueCreateDto,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(queueService.addQueue(queueCreateDto, bindingResult));
    }

    @PostMapping("/edit-queue-information")
//    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<QueueEntity> updateQueueStatus(

            @RequestParam UUID queueId,
            @Valid @RequestBody QueueUpdateDto queueUpdateDto,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult));
    }

    @GetMapping("/getById")
//    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<QueueEntity> getQueueById(

            @RequestParam UUID queueId
    ) {
        return ResponseEntity.ok(queueService.getById(queueId));
    }

    @GetMapping("/cancel-queue")
//    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<QueueEntity> cancelQueue(

            @RequestParam UUID queueId
    ) {
        return ResponseEntity.ok(queueService.cancelQueue(queueId));
    }

    @PostMapping("/count-queues-by-doctorId-and-bookingStatus-active")
    public ResponseEntity<Long> getCountQueuesByDoctorIdAndBookingStatusActive(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(queueService.countDoctorActiveQueues(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/count-queues-by-doctorId-and-bookingStatus-complete")
    public ResponseEntity<Long> getCountQueuesByDoctorIdAndBookingStatusComplete(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(queueService.countDoctorCompletedQueues(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/get-queues-by-doctorId-and-bookingStatus-active")
    public ResponseEntity<List<QueueEntity>> getQueuesByDoctorIdAndBookingStatusActive(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(queueService.getQueuesByDoctorIdAndByQueueStatusActive(UUID.fromString(userDetailsRequestDto.getSource())));
    }

    @PostMapping("/get-queues-by-doctorId-and-bookingStatus-complete")
    public ResponseEntity<List<QueueEntity>> getQueuesByDoctorIdAndBookingStatusComplete(
            @RequestBody UserDetailsRequestDto userDetailsRequestDto
    ) {
        return ResponseEntity.ok(queueService.getQueuesByDoctorIdAndByQueueStatusComplete(UUID.fromString(userDetailsRequestDto.getSource())));
    }


    @GetMapping("/get-doctor-queues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QueueEntity>> getDoctorClientsById(
            @RequestParam UUID doctorId
    ) {
        try {
            List<QueueEntity> queueEntities = queueService.getClientsInDoctorQueue(doctorId);
            return ResponseEntity.ok(queueEntities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
