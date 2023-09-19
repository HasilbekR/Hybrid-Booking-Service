package com.example.hybridbookingservice.service.user;

import com.example.hybridbookingservice.dto.request.UserDetailsRequestDto;
import com.example.hybridbookingservice.dto.request.UserRequestDto;
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
    @Value("${services.get-all-users-by-id}")
    private String getAllUserById;

    public UUID findUserIdByEmail(String email) {
        UserDetailsRequestDto userDetailsRequestDto = new UserDetailsRequestDto(email);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDetailsRequestDto> entity = new HttpEntity<>(userDetailsRequestDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserByEmail),
                HttpMethod.POST,
                entity,
                String.class);
        return UUID.fromString(Objects.requireNonNull(response.getBody()));
    }
    public String findUserEmailById(UUID userId) {
        UserDetailsRequestDto userDetailsRequestDto = new UserDetailsRequestDto(String.valueOf(userId));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDetailsRequestDto> entity = new HttpEntity<>(userDetailsRequestDto, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(getUserById),
                HttpMethod.POST,
                entity,
                String.class);
        System.out.println(Objects.requireNonNull(response.getBody()));
        return Objects.requireNonNull(response.getBody());
    }

    public UserRequestDto userInformation(UUID userId) {
        UserDetailsRequestDto exchangeDataDto = new UserDetailsRequestDto(userId.toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDetailsRequestDto> entity = new HttpEntity<>(exchangeDataDto, httpHeaders);

        ResponseEntity<UserRequestDto> response = restTemplate.exchange(
                getAllUserById,
                HttpMethod.POST,
                entity,
                UserRequestDto.class);

        UserRequestDto userEntity = response.getBody();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFullName(userEntity.getFullName());
        userRequestDto.setPassword(userEntity.getPassword());
        userRequestDto.setPhoneNumber(userEntity.getPhoneNumber());
        userRequestDto.setEmail(userEntity.getEmail());
        userRequestDto.setGender(userEntity.getGender());
        userRequestDto.setDateOfBirth(userEntity.getDateOfBirth());
        userRequestDto.setUserState(userEntity.getUserState());
        userRequestDto.setUserReservationState(userEntity.getUserReservationState());

        return userRequestDto;
    }

}
