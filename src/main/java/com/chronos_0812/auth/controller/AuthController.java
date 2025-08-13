package com.chronos_0812.auth.controller;

import com.chronos_0812.auth.dto.LoginRequest;
import com.chronos_0812.auth.dto.LoginResponse;
import com.chronos_0812.auth.service.LoginService;
import com.chronos_0812.auth.session.SessionContainer;
import com.chronos_0812.auth.session.SessionUser;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.dto.save.UserSaveResponse;
import com.chronos_0812.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 + 로그린 + 로그나웃 전용 API
 * - Lv. 3에서 /auth/signup 구현
 * - POST /auth/signup
 *
 * - Lv.4에서 /auth/login 추가 예정
 * - POST /auth/login
 * - POST /auth/logout
 */

@RestController
@RequiredArgsConstructor
//@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;      // 회원가입
    private final LoginService loginService;    // 로그인 인증 인가
                                                // 로그아웃은??

    /** 회원가입
     * Lv.3 유지: 비밀번호 노출 금지
     */
    @PostMapping("/signup")
    public ResponseEntity<UserSaveResponse> signup(
            @RequestBody UserSaveRequest userSaveRequest
    ) {
        UserSaveResponse userId = userService.save(userSaveRequest);        // 비번 미포함 DTO
        return ResponseEntity.ok(userId);                                   // "일정 관리 회원가입에 성공했습니다."
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
     * - 이메일/비밀번호가 일치하면 HttpSession 생성 후 사용자 정보 SessionUser 에 저장
     * - 예외처리 요구사항 : 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 HTTP Status code 401을 반환합니다.
     */

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest
    ) {
        // 세션 사용자 생성 & 저장
        LoginResponse sessionUser= loginService.login(loginRequest);
//        SessionUser sessionUser= loginService.login(loginRequest);
        if (sessionUser==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 세션 생성 & 저장
        HttpSession httpsession = httpServletRequest.getSession(true); // 없으면 생성
        httpsession.setAttribute(SessionContainer.LOGIN_USER, sessionUser);

        // 세션 타임아웃(선택): 30분
        httpsession.setMaxInactiveInterval(30 * 60);

        return ResponseEntity.ok(new LoginResponse(sessionUser.getUserId(),  sessionUser.getUsername()));
        // 수정 전 : return ResponseEntity.ok(new LoginResponse(user.getId(), user.getUsername()));
        // 수정 후 : return ResponseEntity.ok(new LoginResponse(sessionUser.getId(), sessionUser.getUsername()));
    }

    /**
     * 로그아웃
     * - 세션 무효화
     */

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequestrequest) {
        HttpSession session = httpServletRequestrequest.getSession(false);          // 있으면 가져오고 없으면 null
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
