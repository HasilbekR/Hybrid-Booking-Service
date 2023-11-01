package com.example.hybridbookingservice.service.user;

import com.example.hybridbookingservice.dto.booking.DoctorDetailsForBooking;
import com.example.hybridbookingservice.dto.queue.UserDetailsForQueue;
import com.example.hybridbookingservice.dto.request.ExchangeDataDto;
import com.example.hybridbookingservice.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    private final JwtService jwtService;

    @Value("${services.get-user-email}")
    private String getUserEmail;
    @Value("${services.get-user-id}")
    private String getUserId;
    @Value("${services.get-doctor}")
    private String getUserEntity;
    @Value("${services.get-user}")
    private String getUser;
    @Value("${services.get-hospital-address}")
    private String getHospitalAddress;

    public UUID findUserIdByEmail(String email) {
        HttpEntity<ExchangeDataDto> entity = getInfoFromService(email, "USER-SERVICE");
        ResponseEntity<UUID> response = restTemplate.exchange(
                URI.create(getUserId),
                HttpMethod.POST,
                entity,
                UUID.class);
        return response.getBody();
    }

    public String findUserEmailById(UUID userId) {
        HttpEntity<ExchangeDataDto> entity = getInfoFromService(String.valueOf(userId), "USER-SERVICE");
        return restTemplate.exchange(
                        URI.create(getUserEmail),
                        HttpMethod.POST,
                        entity,
                        String.class).toString();
    }

    public DoctorDetailsForBooking findDoctor(UUID userId) {
        HttpEntity<ExchangeDataDto> entity = getInfoFromService(String.valueOf(userId), "USER-SERVICE");
        ResponseEntity<DoctorDetailsForBooking> response = restTemplate.exchange(
                URI.create(getUserEntity),
                HttpMethod.POST,
                entity,
                DoctorDetailsForBooking.class);
        return Objects.requireNonNull(response.getBody());
    }

    public UserDetailsForQueue findUser(UUID userId) {
        HttpEntity<ExchangeDataDto> entity = getInfoFromService(String.valueOf(userId), "USER-SERVICE");
        ResponseEntity<UserDetailsForQueue> response = restTemplate.exchange(
                URI.create(getUser),
                HttpMethod.POST,
                entity,
                UserDetailsForQueue.class);
        return Objects.requireNonNull(response.getBody());
    }


    public HttpEntity<ExchangeDataDto> getInfoFromService(String source, String service) {
        ExchangeDataDto exchangeDataDto = new ExchangeDataDto(source);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + jwtService.generateAccessTokenForService(service));
        return new HttpEntity<>(exchangeDataDto, httpHeaders);
    }

    public String findHospitalAddress(UUID hospitalId) {
        HttpEntity<ExchangeDataDto> entity = getInfoFromService(String.valueOf(hospitalId), "HOSPITAL-SERVICE");
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getHospitalAddress),
                HttpMethod.POST,
                entity,
                String.class);
        return Objects.requireNonNull(response.getBody());
    }

}
