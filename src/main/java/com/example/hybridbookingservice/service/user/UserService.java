package com.example.hybridbookingservice.service.user;

import com.example.hybridbookingservice.dto.request.UserBookingRequestDto;
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

    @Value("${services.get-by-user-id}")
    private String getUserById;
    @Value("${services.get-by-user-email}")
    private String getUserByEmail;

    public UUID findUserIdByEmail(String email) {
        UserBookingRequestDto userBookingRequestDto = new UserBookingRequestDto(email);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserBookingRequestDto> entity = new HttpEntity<>(userBookingRequestDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserByEmail),
                HttpMethod.POST,
                entity,
                String.class);
        return UUID.fromString(Objects.requireNonNull(response.getBody()));
    }
    public String  findUserEmailById(UUID userId) {
        UserBookingRequestDto userBookingRequestDto = new UserBookingRequestDto(String.valueOf(userId));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserBookingRequestDto> entity = new HttpEntity<>(userBookingRequestDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserById),
                HttpMethod.POST,
                entity,
                String.class);
        System.out.println(Objects.requireNonNull(response.getBody()));
        return Objects.requireNonNull(response.getBody());
    }

}
