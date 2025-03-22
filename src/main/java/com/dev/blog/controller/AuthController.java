package com.dev.blog.controller;

import com.dev.blog.domain.dtos.AuthResponse;
import com.dev.blog.domain.dtos.LoginRequest;
import com.dev.blog.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid  @RequestBody LoginRequest loginRequest) {
        log.info( "Login user with email {}", loginRequest.getEmail() );

        UserDetails user = authenticationService.login(loginRequest);

        AuthResponse authResponse = AuthResponse.builder()
                .token(authenticationService.generateToken(user))
                .expiresIn(86400) // 24 hours in seconds
                .build();

        return ResponseEntity.ok(authResponse);
    }


}
