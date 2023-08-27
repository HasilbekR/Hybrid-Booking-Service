package com.example.hybridbookingservice.service.queue;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.repository.queue.QueueRepository;
import com.example.hybridbookingservice.service.doctor.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueueServiceTest {
    private QueueUpdateDto queueUpdateDto;
    @Mock
    private QueueRepository queueRepository;
    @InjectMocks
    private QueueService queueService;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    public void setUp() {
        queueRepository = mock(QueueRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        queueService = new QueueService(queueRepository, doctorService, modelMapper);
    }

    @Test
    public void testAddQueueWithNoErrors() {
        QueueCreateDto queueCreateDto = new QueueCreateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        LocalDate currentDate = LocalDate.now();
        when(queueRepository.findMaxQueueNumberByQueueDateAndDoctorId(currentDate, queueCreateDto.getDoctorId())).thenReturn(10L);

        QueueEntity expectedQueueEntity = new QueueEntity();
        when(queueRepository.save(any(QueueEntity.class))).thenReturn(expectedQueueEntity);

        QueueEntity result = queueService.addQueue(queueCreateDto, bindingResult);

        assertEquals(expectedQueueEntity, result);
    }

    @Test
    public void testAddQueueWithErrors() {
        QueueCreateDto queueCreateDto = new QueueCreateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        List<ObjectError> errors = Collections.singletonList(new ObjectError("field", "error message"));
        when(bindingResult.getAllErrors()).thenReturn(errors);

        assertThrows(RequestValidationException.class, () -> {
            queueService.addQueue(queueCreateDto, bindingResult);
        });

    }

    @Test
    public void testEditQueueInformationWithValidData() {
        UUID queueId = UUID.randomUUID();
        QueueUpdateDto queueUpdateDto = new QueueUpdateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        QueueEntity existingQueueEntity = new QueueEntity();
        when(queueRepository.findById(queueId)).thenReturn(Optional.of(existingQueueEntity));

        QueueEntity updatedQueueEntity = new QueueEntity();
        when(queueRepository.save(any(QueueEntity.class))).thenReturn(updatedQueueEntity);

        QueueEntity result = queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult);

        assertEquals(updatedQueueEntity, result);
    }

    @Test
    public void testEditQueueInformationWithInvalidData() {
        UUID queueId = UUID.randomUUID();
        QueueUpdateDto queueUpdateDto = new QueueUpdateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        List<ObjectError> errors = Collections.singletonList(new ObjectError("field", "error message"));
        when(bindingResult.getAllErrors()).thenReturn(errors);

        assertThrows(RequestValidationException.class, () -> {
            queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult);
        });
    }

    @Test
    public void testEditQueueInformationWithNotFound() {
        UUID queueId = UUID.randomUUID();
        QueueUpdateDto queueUpdateDto = new QueueUpdateDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(queueRepository.findById(queueId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult);
        });
    }

    @Test
    public void testUpdateStatus_ValidationError() {
        UUID queueId = UUID.randomUUID();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(RequestValidationException.class, () -> queueService.editQueueInformation(queueId, queueUpdateDto, bindingResult));

    }

    @Test
    public void testGetById_Success() {
        UUID queueId = UUID.randomUUID();
        QueueEntity queueEntity = new QueueEntity();
        when(queueRepository.findById(queueId)).thenReturn(Optional.of(queueEntity));

        QueueEntity retrievedQueue = queueService.getById(queueId);

        assertEquals(queueEntity, retrievedQueue);

    }

    @Test
    public void testGetById_DataNotFoundException() {
        UUID queueId = UUID.randomUUID();
        when(queueRepository.findById(queueId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> queueService.getById(queueId));

    }

    @Test
    public void testCancelQueue() {
        UUID queueId = UUID.randomUUID();
        when(queueRepository.findById(queueId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> queueService.cancelQueue(queueId));
    }

}