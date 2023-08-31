package com.example.hybridbookingservice.service.user;

import com.example.hybridbookingservice.dto.request.ExchangeDataDto;
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

    @Value("${services.get-user-email}")
    private String getUserEmail;
    @Value("${services.get-user-id}")
    private String getUserId;

    public UUID findUserIdByEmail(String email) {
        ExchangeDataDto exchangeDataDto = new ExchangeDataDto(email);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExchangeDataDto> entity = new HttpEntity<>(exchangeDataDto, httpHeaders);
        ResponseEntity<UUID> response = restTemplate.exchange(
                URI.create(getUserId),
                HttpMethod.POST,
                entity,
                UUID.class);
        return response.getBody();
    }
    public String  findUserEmailById(UUID userId) {
        ExchangeDataDto exchangeDataDto = new ExchangeDataDto(String.valueOf(userId));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExchangeDataDto> entity = new HttpEntity<>(exchangeDataDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserEmail),
                HttpMethod.POST,
                entity,
                String.class);
        return Objects.requireNonNull(response.getBody());
    }

}
