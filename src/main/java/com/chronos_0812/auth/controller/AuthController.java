package com.chronos_0812.auth.controller;

import com.chronos_0812.auth.dto.AuthResponse;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
            @RequestBody UserSaveRequest userSaveRequest
    ) {
        Long userId = userService.save(userSaveRequest);
        return ResponseEntity.ok(userId);  // "일정 관리 회원가입에 성공했습니다."
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody UserSaveRequest userSaveRequest
    ) {
//        // Cookie Session을 발급
//        AuthResponse result = authService.login(authRequest);
//
//        HttpSession session = request.getSession(); // 신규 세션 생성, JSESSIONID 쿠키 발급
//        session.setAttribute("LOGIN_DIRECTOR", result.getId()); // 서버 메모리에 세션 저장
//        return "영화 감독 로그인에 성공했습니다.";
//    }

//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request) {
//        // 로그인하지 않으면 HttpSession이 null로 반환된다.
//        HttpSession session = request.getSession(false);
//        // 세션이 존재하면 -> 로그인이 된 경우
//        if (session != null) {
//            session.invalidate(); // 해당 세션(데이터)을 삭제한다.
//        }
//    }
}
