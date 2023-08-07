package com.example.hybridbookingservice.service.queue;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.repository.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final ModelMapper modelMapper;
    private final Long queueNumber = 0L;

    public QueueEntity addQueue(QueueCreateDto queueCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        QueueEntity queueEntity = modelMapper.map(queueCreateDto, QueueEntity.class);

        LocalDate currentDate = LocalDate.now();

        Long lastQueueNumber = queueRepository.findMaxQueueNumberByDateAndByDoctorId(currentDate, queueCreateDto.getDoctorId());
        if (lastQueueNumber == null) {
            lastQueueNumber = 0L;
        }
        Long newQueueNumber = lastQueueNumber + 1;

        queueEntity.setQueueNumber(newQueueNumber);
        queueEntity.setQueueEntityStatus(QueueEntityStatus.ACTIVE);
        queueEntity.setQueueDate(currentDate);

        return queueRepository.save(queueEntity);
    }

    public QueueEntity editQueueInformation(UUID queueId, QueueUpdateDto queueCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        QueueEntity queueEntity = queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        modelMapper.map(queueCreateDto, queueEntity);
        return queueRepository.save(queueEntity);
    }

    public QueueEntity getById(UUID queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("This queue not found"));
    }

    public QueueEntity cancelQueue(UUID queueId) {
        QueueEntity queueForCancel = queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("This queue not found"));

        queueRepository.update(QueueEntityStatus.SKIPPED, queueId);
        return queueForCancel;
    }

}
