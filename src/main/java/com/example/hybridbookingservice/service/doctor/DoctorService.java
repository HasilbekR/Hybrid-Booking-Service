package com.example.hybridbookingservice.service.doctor;

import com.example.hybridbookingservice.dto.request.DoctorRequestDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.UUID;
@Service
public class DoctorService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseApiUrl = "http:808//example.com/api"; // Replace with your actual API base URL

    public DoctorRequestDto getDoctorById(UUID doctorId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String getDoctorByIdUrl = baseApiUrl + "/doctors/" + doctorId; // Adjust the URL pattern according to your API

        ResponseEntity<DoctorRequestDto> response = restTemplate.exchange(
                URI.create(getDoctorByIdUrl),
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DoctorRequestDto.class);

        return response.getBody();
    }
}
