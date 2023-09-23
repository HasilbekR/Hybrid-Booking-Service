package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueResultForFront;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.dto.response.StandardResponse;
import com.example.hybridbookingservice.dto.response.Status;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.service.queue.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hybrid-booking/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;
    @PostMapping("/add-queue")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public StandardResponse<QueueResultForFront> addQueue(

            @Valid @RequestBody QueueCreateDto queueCreateDto,
            BindingResult bindingResult
    ) {
        return StandardResponse.<QueueResultForFront>builder()
                .status(Status.SUCCESS)
                .message("Queue successfully added")
                .data(queueService.addQueue(queueCreateDto, bindingResult).getData())
                .build();
    }

    @PostMapping("/edit-queue-information")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public StandardResponse<QueueEntity> updateQueueStatus(

            @RequestParam UUID queueId,
            @Valid @RequestBody QueueUpdateDto queueUpdateDto,
            BindingResult bindingResult
    ) {
        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Queue information successfully updated")
                .data(queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult).getData())
                .build();
    }

    @GetMapping("/getById")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public StandardResponse<QueueEntity> getQueueById(

            @RequestParam UUID queueId
    ) {
        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Queue information by id")
                .data(queueService.getById(queueId).getData())
                .build();
    }

    @GetMapping("/cancel-queue")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public StandardResponse<QueueEntity> cancelQueue(

            @RequestParam UUID queueId
    ) {
        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.SUCCESS)
                .message("cancelled queue")
                .data(queueService.cancelQueue(queueId).getData())
                .build();
    }

    @GetMapping("/get-doctor-queues")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<List<QueueEntity>> getDoctorClientsById(
            @RequestParam UUID doctorId
    ) {
        try {
            List<QueueEntity> queueEntities = queueService.getClientsInDoctorQueue(doctorId);
            return StandardResponse.<List<QueueEntity>>builder()
                    .status(Status.SUCCESS)
                    .message("Doctor queues")
                    .data(queueEntities)
                    .build();
        } catch (Exception e) {
            return StandardResponse.<List<QueueEntity>>builder()
                    .status(Status.ERROR)
                    .message("An error occurred while retrieving doctor queue data")
                    .data(Collections.emptyList())
                    .build();
        }
    }



}
