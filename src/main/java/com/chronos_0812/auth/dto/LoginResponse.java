package com.chronos_0812.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 성공 응답
 */

@Getter
@NoArgsConstructor

public class LoginResponse {

    private Long userId;
    private String username;

    public LoginResponse(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
