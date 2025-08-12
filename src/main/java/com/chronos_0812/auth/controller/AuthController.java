package com.chronos_0812.auth.controller;

import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 전용 API
 * - POST /auth/signup
 * - Lv.4에서 /auth/login 추가 예정
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(
            @RequestBody UserSaveRequest userSaveRequest) {
        Long userId = userService.save(userSaveRequest);
        return ResponseEntity.ok(userId);
    }
}
