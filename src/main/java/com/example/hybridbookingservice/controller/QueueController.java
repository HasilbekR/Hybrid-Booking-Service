package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.queue.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return queueService.addQueue(queueCreateDto);
    }

    @PutMapping("/update-queue-status")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity updateQueueStatus(
            @Valid @RequestBody QueueUpdateDto queueUpdateDto,
            BindingResult bindingResult
    ) {
        return queueService.updateStatus(queueUpdateDto.getQueueId(), queueUpdateDto.getStatus(), bindingResult);
    }

    @GetMapping("/getById")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public QueueEntity geQueueById(
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
