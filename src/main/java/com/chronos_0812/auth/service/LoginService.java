package com.chronos_0812.auth.service;

import com.chronos_0812.auth.dto.LoginRequest;
import com.chronos_0812.auth.dto.LoginResponse;
import com.chronos_0812.auth.session.SessionUser;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 인증 인가??
 * LoginRequest의 필드
 * private String email;
 * private String password;
 *
 *
 * */

///  AuthoController 에서 loginService.authenticate 문제..

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public SessionUser authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user==null) { return null; }
        if (!user.getPassword().equals(password)) { return null; }
        SessionUser sessionUser = new SessionUser();
        return sessionUser;
    }

    ///  비교
    // .orElse()
    // .orElseThrow()

    // 언제 어디서 가져왔는지 모를 코드..
//    @Transactional
//    public void signup(LoginRequest loginRequest) {
//        User user = new User(loginRequest.getUsername());
//        userRepository.save(user);
//    }
//
//    @Transactional(readOnly = true)
//    public LoginResponse login(LoginRequest loginRequest) {
//        User user = userRepository.findById(loginRequest.getUsername()).orElseThrow(
//                () -> new IllegalArgumentException("없는 유저입니다.")
//        );
//    }
}
