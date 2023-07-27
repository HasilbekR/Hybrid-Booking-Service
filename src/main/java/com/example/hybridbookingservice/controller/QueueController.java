package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.service.queue.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/add-queue")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity addQueue(
            @Valid @RequestBody QueueCreateDto queueCreateDto,
            BindingResult bindingResult
    ) {
        return queueService.addQueue(queueCreateDto, bindingResult);
    }

    @PutMapping("/edit-queue-information")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity updateQueueStatus(
            @RequestParam UUID queueId,
            @Valid @RequestBody QueueUpdateDto queueUpdateDto,
            BindingResult bindingResult
    ) {
        return queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult);
    }

    @GetMapping("/getById")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity getQueueById(
            @RequestParam UUID queueId
    ) {
        return queueService.getById(queueId);
    }

    @GetMapping("/cancel-queue")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity cancelQueue(
            @RequestParam UUID queueId
    ) {
        return queueService.cancelQueue(queueId);
    }
}
