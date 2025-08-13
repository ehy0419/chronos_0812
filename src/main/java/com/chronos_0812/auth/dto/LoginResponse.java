package com.chronos_0812.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 성공 응답
 */

@Getter

public class LoginResponse {

    private final Long userId;
    private final String username;

    public LoginResponse(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
