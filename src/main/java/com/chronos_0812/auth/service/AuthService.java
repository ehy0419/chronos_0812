package com.chronos_0812.auth.service;

import com.chronos_0812.auth.dto.AuthRequest;
import com.chronos_0812.auth.dto.AuthResponse;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(AuthRequest authRequest) {
        User user = new User(authRequest.getUsername());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findById(authRequest.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("없는 유저입니다.")
        );

    }
}
