package com.dev.blog.service.impl;

import com.dev.blog.domain.dtos.LoginRequest;
import com.dev.blog.service.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 86400000L;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;


    @Override
    public UserDetails login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword() )
        );
        return userDetailsService.loadUserByUsername( loginRequest.getEmail() );
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims( claims )
                .setSubject( userDetails.getUsername() )
                .setIssuedAt( new java.util.Date( System.currentTimeMillis() ) )
                .setExpiration( new java.util.Date( System.currentTimeMillis() + jwtExpiryMs ) )
                .signWith( getSigningKey(), SignatureAlgorithm.HS256 )
                .compact();
    }


    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor( keyBytes );
    }
}
