package com.dev.blog.service;


import com.dev.blog.domain.dtos.AuthResponse;
import com.dev.blog.domain.dtos.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails login(LoginRequest loginRequest);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);

}
