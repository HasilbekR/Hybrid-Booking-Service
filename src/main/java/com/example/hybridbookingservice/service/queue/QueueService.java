package com.example.hybridbookingservice.service.queue;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.repository.queue.QueueRepository;
import com.example.hybridbookingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final ModelMapper modelMapper;
    private final Long queueNumber = 0L;

    public QueueEntity addQueue(QueueCreateDto queueCreateDto) {
        QueueEntity queueEntity = modelMapper.map(queueCreateDto, QueueEntity.class);
        Long lastQueueNumber = queueRepository.findMaxQueueNumber();
        if (lastQueueNumber == null) {
            lastQueueNumber = 0L;
        }
        Long newQueueNumber = lastQueueNumber + 1;

        queueEntity.setQueueNumber(newQueueNumber);
        queueEntity.setQueueEntityStatus(QueueEntityStatus.ACTIVE);
        return queueRepository.save(queueEntity);
    }

    public QueueEntity updateStatus(UUID queueId, QueueEntityStatus queueEntityStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        QueueEntity queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("This queue not found"));

        queueRepository.update(queueEntityStatus, queueId);
        return queue;
    }

    public QueueEntity getById(UUID queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("This queue not found"));
    }

    public QueueEntity cancelQueue(UUID queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("This queue not found"));
    }

//    public HttpStatus transferQueue(QueueTransferDto queueTransferDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<ObjectError> allErrors = bindingResult.getAllErrors();
//            throw new RequestValidationException(allErrors);
//        }
//
//        String senderId = userRepository.findById(queueTransferDto.getSenderId());
//
//        String receiverId = userRepository.findById(queueTransferDto.getReceiverId());
//
//        queueRepository.getActiveQueue()
//
//    }

}
