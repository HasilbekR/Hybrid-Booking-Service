package com.example.hybridbookingservice.service.doctor;

import com.example.hybridbookingservice.dto.request.DoctorRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {


    private final RestTemplate restTemplate;

    @Value("${services.get-by-doctor-id}")
    private String getDoctorById;

    public DoctorRequestDto getDoctorById(UUID doctorId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<DoctorRequestDto> response = restTemplate.exchange(
                URI.create(getDoctorById),
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DoctorRequestDto.class);

        return response.getBody();
    }


}
