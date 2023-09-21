package com.example.hybridbookingservice.service.queue;

import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.dto.request.DoctorRequestDto;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.service.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    public QueueEntity addQueue(QueueCreateDto queueCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        QueueEntity queueEntity = modelMapper.map(queueCreateDto, QueueEntity.class);

        LocalDate currentDate = LocalDate.now();

        Long lastQueueNumber = queueRepository.findMaxQueueNumberByQueueDateAndDoctorId(currentDate, queueCreateDto.getDoctorId());

        if (lastQueueNumber == null) {
            lastQueueNumber = 0L;
        }

        Long newQueueNumber = lastQueueNumber + 1;
        queueEntity.setQueueNumber(newQueueNumber);
        queueEntity.setQueueEntityStatus(QueueEntityStatus.ACTIVE);
        queueEntity.setQueueDate(currentDate);
        return queueRepository.save(queueEntity);
    }

    public QueueEntity editQueueInformation(UUID queueId, QueueUpdateDto queueUpdateDto, BindingResult bindingResult) {
        validateBindingResult(bindingResult);

        QueueEntity queueEntity = getQueueById(queueId);
        modelMapper.map(queueUpdateDto, queueEntity);

        return queueRepository.save(queueEntity);
    }

    public QueueEntity getById(UUID queueId) {
        return getQueueById(queueId);
    }

    public QueueEntity cancelQueue(UUID queueId) {
        QueueEntity queueForCancel = getQueueById(queueId);
        queueForCancel.setQueueEntityStatus(QueueEntityStatus.SKIPPED);
        return queueRepository.save(queueForCancel);
    }

    public Optional<QueueEntity> getActiveQueueByUserIdAndStatusIsNull(UUID userId) {
        return queueRepository.findByUserIdAndQueueEntityStatusIsNull(userId);
    }

    @Transactional
    public void updateQueueEntityStatusById(UUID queueId, QueueEntityStatus status) {
        queueRepository.updateQueueEntityStatusById(status, queueId);
    }

    public List<QueueEntity> getClientsInDoctorQueue(UUID doctorId) {
        DoctorRequestDto doctor = doctorService.getDoctorById(doctorId);
        return queueRepository.findByDoctorIdAndQueueEntityStatus(doctor.getDoctorId(), QueueEntityStatus.ACTIVE);
    }

    private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid input data");
        }
    }

    private QueueEntity getQueueById(UUID queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("Queue not found"));
    }

    public Long countDoctorActiveQueues(UUID doctorId) {
        return queueRepository.countDoctorQueuesByBookingStatus(doctorId, QueueEntityStatus.ACTIVE);
    }

    public Long countDoctorCompletedQueues(UUID doctorId) {
        return queueRepository.countDoctorQueuesByBookingStatus(doctorId, QueueEntityStatus.COMPLETED);
    }

    public List<QueueEntity> getQueuesByDoctorIdAndByQueueStatusActive(UUID doctorId) {
        return queueRepository.getQueuesByDoctorIdAndBookingStatus(doctorId, QueueEntityStatus.ACTIVE);
    }

    public List<QueueEntity> getQueuesByDoctorIdAndByQueueStatusComplete(UUID doctorId) {
        return queueRepository.getQueuesByDoctorIdAndBookingStatus(doctorId, QueueEntityStatus.COMPLETED);
    }


//    private void initializeQueueEntity(QueueEntity queueEntity, UUID doctorId) {
//        LocalDate currentDate = LocalDate.now();
//        Long lastQueueNumber = queueRepository.findMaxQueueNumberByQueueDateAndDoctorId(currentDate, doctorId);
//        Long newQueueNumber = (lastQueueNumber == null) ? 1L : lastQueueNumber + 1;
//
//        queueEntity.setQueueNumber(newQueueNumber);
//        queueEntity.setQueueEntityStatus(QueueEntityStatus.ACTIVE);
//        queueEntity.setQueueDate(currentDate);
//    }
}
