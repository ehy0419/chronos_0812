package com.chronos_0812.auth.session;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 세션에 저장할 최소한의 유저 정보 (비밀번호 제외)
 */

@Getter
@NoArgsConstructor

public class SessionUser {
    private Long id;
    private String username;
    private String email;
    /// 세션에는 비밀번호 제외 id/username/email 만 저장!!///
}
