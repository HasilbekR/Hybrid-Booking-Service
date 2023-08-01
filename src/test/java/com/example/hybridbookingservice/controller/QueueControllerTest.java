package com.example.hybridbookingservice.controller;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.service.queue.QueueService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QueueControllerTest {

    @Mock
    private QueueService queueService;

    @InjectMocks
    private QueueController queueController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAddQueue() {
        QueueCreateDto queueCreateDto = new QueueCreateDto();
        queueCreateDto.setUserId(UUID.randomUUID());
        queueCreateDto.setDoctorId(UUID.randomUUID());

        QueueEntity mockQueueEntity = new QueueEntity();
        when(queueService.addQueue(any(QueueCreateDto.class), any(BindingResult.class)))
                .thenReturn(mockQueueEntity);

        QueueEntity result = queueController.addQueue(queueCreateDto, mock(BindingResult.class));

        verify(queueService).addQueue(any(QueueCreateDto.class), any(BindingResult.class));
        assertEquals(mockQueueEntity, result);
    }


    @Test
    void testUpdateQueueStatus() {
        QueueUpdateDto queueUpdateDto = new QueueUpdateDto();
        queueUpdateDto.setQueueEntityStatus(QueueEntityStatus.ACTIVE);

        UUID queueId = UUID.randomUUID();
        QueueEntity mockQueueEntity = new QueueEntity();
        when(queueService.editQueueInformation(any(UUID.class), any(QueueUpdateDto.class), any(BindingResult.class)))
                .thenReturn(mockQueueEntity);

        QueueEntity result = queueController.updateQueueStatus(queueId, queueUpdateDto, mock(BindingResult.class));

        verify(queueService).editQueueInformation(any(UUID.class), any(QueueUpdateDto.class), any(BindingResult.class));
        assertEquals(mockQueueEntity, result);
    }

}
