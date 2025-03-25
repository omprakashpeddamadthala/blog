package com.dev.blog.service.impl;

import com.dev.blog.domain.entities.User;
import com.dev.blog.repository.UserRepository;
import com.dev.blog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById( id )
                .orElseThrow( () -> new EntityNotFoundException( "User with id " + id + " not found" ) );
    }
}
