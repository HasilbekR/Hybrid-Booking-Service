package com.example.hybridbookingservice.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public Jws<Claims> extractToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }
    public String generateAccessTokenForService(String receiverService){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setIssuer("HYBRID-BOOKING-SERVICE")
                .setSubject(receiverService)
                .addClaims(Map.of("authorities", List.of("ROLE_SENDER")))
                .compact();
    }
}
