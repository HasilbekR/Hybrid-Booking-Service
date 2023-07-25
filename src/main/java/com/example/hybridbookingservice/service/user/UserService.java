package com.example.hybridbookingservice.service.user;

import com.example.hybridbookingservice.dto.request.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;

    @Value("${services.get-by-user-id}")
    private String getUserById;

    public String findById(UUID userId) {
        UserRequestDto userRequestDto = new UserRequestDto(userId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequestDto> entity = new HttpEntity<>(userRequestDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserById + "/" + userId),
                HttpMethod.GET,
                entity,
                String.class);
        return response.getBody();
    }
}
