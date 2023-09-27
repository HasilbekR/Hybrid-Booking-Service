package com.example.hybridbookingservice.service.queue;

import com.example.hybridbookingservice.dto.booking.DoctorDetailsForBooking;
import com.example.hybridbookingservice.dto.queue.QueueCreateDto;
import com.example.hybridbookingservice.dto.queue.QueueResultForFront;
import com.example.hybridbookingservice.dto.queue.QueueUpdateDto;
import com.example.hybridbookingservice.dto.queue.UserDetailsForQueue;
import com.example.hybridbookingservice.dto.request.DoctorRequestDto;
import com.example.hybridbookingservice.dto.response.StandardResponse;
import com.example.hybridbookingservice.dto.response.Status;
import com.example.hybridbookingservice.entity.queue.QueueEntity;
import com.example.hybridbookingservice.entity.queue.QueueEntityStatus;
import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import com.example.hybridbookingservice.exceptions.RequestValidationException;
import com.example.hybridbookingservice.repository.queue.QueueRepository;
import com.example.hybridbookingservice.service.doctor.DoctorService;
import com.example.hybridbookingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final DoctorService doctorService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public StandardResponse<QueueResultForFront> addQueue(QueueCreateDto queueCreateDto, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        // Retrieve user and doctor details
        UserDetailsForQueue user = userService.findUser(queueCreateDto.getUserId());
        DoctorDetailsForBooking doctor = userService.findDoctor(queueCreateDto.getDoctorId());

        // Retrieve hospital address
        String hospitalAddress = userService.findHospitalAddress(doctor.getHospitalId());

        // Create and save the queue entity
        QueueEntity queueEntity = modelMapper.map(queueCreateDto, QueueEntity.class);
        LocalDate currentDate = LocalDate.now();
        QueueEntity lastQueueNumber = queueRepository.findTopByQueueDateAndDoctorIdOrderByQueueNumberDesc(currentDate, queueCreateDto.getDoctorId());

        long newQueueNumber;

        if (lastQueueNumber == null) {
            // No queue entry for the current date, start from 1
            newQueueNumber = 1;
        } else {
            // Increment the queue number
            newQueueNumber = lastQueueNumber.getQueueNumber() + 1;
        }

        queueEntity.setQueueNumber(newQueueNumber);
        queueEntity.setQueueDate(currentDate);
        queueEntity.setQueueEntityStatus(QueueEntityStatus.ACTIVE);
        queueRepository.save(queueEntity);

        List<QueueResultForFront> queueResultForFrontList = new ArrayList<>();
        // Prepare the response


        return StandardResponse.<QueueResultForFront>builder()
                .status(Status.SUCCESS)
                .message("Queue information")
                .data(QueueResultForFront.builder()
                        .userName(user.getName())
                        .doctorName(doctor.getFullName())
                        .queueCreatedDate(String.valueOf(queueEntity.getCreatedDate()))
                        .queueCreatedTime(queueEntity.getCreatedDate().toLocalTime().toString())
                        .roomNumber(doctor.getRoomNumber())
                        .specialty(doctor.getSpecialty())
                        .address(hospitalAddress)
                        .queueNumber(String.valueOf(newQueueNumber)) // Include the queue number in the response
                        .build())
                .build();
    }

    public StandardResponse<QueueEntity> editQueueInformation(UUID queueId, QueueUpdateDto queueUpdateDto, BindingResult bindingResult) {
        validateBindingResult(bindingResult);

        QueueEntity queueEntity = getQueueById(queueId);
        modelMapper.map(queueUpdateDto, queueEntity);

        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.ERROR)
                .message("Edit queue information successfully updated")
                .data(queueRepository.save(queueEntity))
                .build();
    }

    public StandardResponse<QueueEntity> getById(UUID queueId) {
        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Queue information by id")
                .data(getQueueById(queueId))
                .build();
    }

    public StandardResponse<QueueEntity> cancelQueue(UUID queueId) {
        QueueEntity queueForCancel = getQueueById(queueId);
        queueForCancel.setQueueEntityStatus(QueueEntityStatus.SKIPPED);
        return StandardResponse.<QueueEntity>
                builder()
                .status(Status.SUCCESS)
                .message("Queue cancelled")
                .data(queueRepository.save(queueForCancel))
                .build();
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
