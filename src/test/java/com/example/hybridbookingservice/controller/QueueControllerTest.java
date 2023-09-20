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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        BindingResult bindingResult = mock(BindingResult.class);
        QueueEntity expectedQueueEntity = new QueueEntity();

        when(queueService.addQueue(eq(queueCreateDto), eq(bindingResult)))
                .thenReturn(expectedQueueEntity);

        ResponseEntity<QueueEntity> response = queueController.addQueue(queueCreateDto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedQueueEntity, response.getBody());
        verify(queueService, times(1)).addQueue(eq(queueCreateDto), eq(bindingResult));
    }



    @Test
    void testUpdateQueueStatus() {
        QueueUpdateDto queueUpdateDto = new QueueUpdateDto();
        queueUpdateDto.setQueueEntityStatus(QueueEntityStatus.ACTIVE);

        UUID queueId = UUID.randomUUID();
        QueueEntity mockQueueEntity = new QueueEntity();

        BindingResult bindingResult = mock(BindingResult.class);

        when(queueService.editQueueInformation(any(UUID.class), any(QueueUpdateDto.class), eq(bindingResult)))
                .thenReturn(mockQueueEntity);

        ResponseEntity<QueueEntity> response = queueController.updateQueueStatus(queueId, queueUpdateDto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQueueEntity, response.getBody());
        verify(queueService).editQueueInformation(eq(queueId), eq(queueUpdateDto), eq(bindingResult));
        assertEquals(mockQueueEntity, response.getBody());
    }

    @Test
    public void testGetQueueById() {
        UUID queueId = UUID.randomUUID();
        QueueEntity mockQueueEntity = new QueueEntity();
        when(queueService.getById(eq(queueId))).thenReturn(mockQueueEntity);

        ResponseEntity<QueueEntity> response = queueController.getQueueById(queueId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQueueEntity, response.getBody());
        verify(queueService, times(1)).getById(eq(queueId));
    }

    @Test
    public void testCancelQueue() {
        UUID queueId = UUID.randomUUID();
        QueueEntity mockQueueEntity = new QueueEntity();
        when(queueService.cancelQueue(eq(queueId))).thenReturn(mockQueueEntity);

        ResponseEntity<QueueEntity> response = queueController.cancelQueue(queueId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQueueEntity, response.getBody());
        verify(queueService, times(1)).cancelQueue(eq(queueId));
    }

    @Test
    public void testGetDoctorClientsById() {
        UUID doctorId = UUID.randomUUID();
        List<QueueEntity> mockQueueEntities = new ArrayList<>();
        when(queueService.getClientsInDoctorQueue(eq(doctorId))).thenReturn(mockQueueEntities);

        ResponseEntity<List<QueueEntity>> response = queueController.getDoctorClientsById(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQueueEntities, response.getBody());
        verify(queueService, times(1)).getClientsInDoctorQueue(eq(doctorId));
    }

    @Test
    public void testGetDoctorClientsByIdAccessDenied() {
        UUID doctorId = UUID.randomUUID();
        when(queueService.getClientsInDoctorQueue(eq(doctorId))).thenThrow(new AccessDeniedException("Access denied"));

        ResponseEntity<List<QueueEntity>> response = queueController.getDoctorClientsById(doctorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(queueService, times(1)).getClientsInDoctorQueue(eq(doctorId));
    }
}
