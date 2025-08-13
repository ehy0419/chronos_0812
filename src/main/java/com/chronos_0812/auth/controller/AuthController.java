package com.chronos_0812.auth.controller;

import com.chronos_0812.auth.dto.LoginResponse;
import com.chronos_0812.auth.session.SessionUser;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

/**
 * 회원가입은 Lv3에서 구현 완료 (/auth/signup)
 * 여기서는 로그인/로그아웃만 추가
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

    //@PostMapping("/signup")
    //    public String signup(
    //            @RequestBody AuthRequest request
    //    ) {
    //        authService.signup(request);
    //        return "영화 감독 회원가입에 성공했습니다.";
    //    }

    /**
     * 로그인
     * - 이메일/비밀번호가 일치하면 HttpSession 생성 후 사용자 정보 저장
     * - 과제 요구사항 : 불일치 시 401 반환
     */

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody UserSaveRequest userSaveRequest, HttpServletRequest httpServletRequest
    ) {
        if (userSaveRequest.getEmail().equals(userSaveRequest.getPassword())) {
            HttpSession httpSession = httpServletRequest.getSession();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElse(null);

        // Lv6에서: passwordEncoder.matches(req.getPassword(), user.getPassword())
        if (user == null || !req.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 세션 생성 & 저장
        HttpSession session = request.getSession(true); // 없으면 생성
        session.setAttribute(SessionConst.LOGIN_USER,
                new SessionUser(user.getId(), user.getUsername(), user.getEmail()));

        // 세션 타임아웃(선택): 30분
        session.setMaxInactiveInterval(30 * 60);

        return ResponseEntity.ok(new LoginResponse(user.getId(), user.getUsername()));
    }

    /**
     * 로그아웃
     * - 세션 무효화
     */

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 있으면 가져오고 없으면 null
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    //@PostMapping("/login")
    //    public String login(
    //            @RequestBody AuthRequest authRequest,
    //            HttpServletRequest request
    //    ) {
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
