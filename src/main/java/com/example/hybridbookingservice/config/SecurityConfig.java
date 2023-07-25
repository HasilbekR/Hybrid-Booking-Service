package com.example.hybridbookingservice.config;

import com.example.hybridbookingservice.filter.JwtTokenFilter;
import com.example.hybridbookingservice.service.auth.AuthenticationService;
import com.example.hybridbookingservice.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfig {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
//                .authorizeHttpRequests((authorizer) -> {
//                    authorizer
//
//                })
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(authenticationService, jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();

    }

//    @Bean
//    public AuthenticationService authManager(HttpSecurity httpSecurity) {
//        AuthenticationManagerBuilder authenticationManagerBuilder
//                = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService()
//    }


}
