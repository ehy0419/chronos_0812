package com.chronos_0812.auth.dto;

import lombok.Getter;

@Getter

public class AuthResponse {

    private final Long userId;

    public AuthResponse(Long userId) {
        this.userId = userId;
    }
}
