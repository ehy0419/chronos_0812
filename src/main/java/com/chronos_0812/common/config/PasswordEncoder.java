package com.chronos_0812.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bcrypt 기반 비밀번호 인코더
 * - encode: 평문 → 해시("$2a$.." 형태)
 * - matches: 평문 vs 해시 비교
 */

@Component
public class PasswordEncoder {

    // 가변 cost 사용을 위해서 필드 선언
    @Value("${security.bcrypt.cost:10}")        // 개발에서는 10, 운영 단계에서는 12 권장
    private int cost;

    /** 평문 비밀번호를 해시로 변환 */
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
        // 수정 전: BCrypt.MIN_COST (매우 약함)
        //return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
        // 수정 후: 가변 cost(기본 12) 사용
        //return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
    }

    /** 평문 ↔ 해시 일치 여부 확인 (재해시 금지, 반드시 verify 사용) */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt
                .verifyer()
                .verify(rawPassword.toCharArray(),
                        encodedPassword);
        return result.verified;
    }
}
