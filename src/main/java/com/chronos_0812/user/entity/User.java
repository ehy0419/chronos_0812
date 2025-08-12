package com.chronos_0812.user.entity;

import com.chronos_0812.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Lv2: 유저 모델
 * - 필수: username, email (+ BaseEntity의 createdAt/updatedAt)
 * - email UNIQUE 제약
 * - 비밀번호는 Lv3에서 추가 예정
 *
 * <주의>
 * 이미 프로젝트에 password 필드가 있다면 Lv3 전까지는 nullable=true 로 완화하거나, 컨트롤러에서 임시 값 세팅하세요.
 * </주의>
 */

/**
 * Lv3: 회원가입을 위해 password 필드 추가.
 *  - 이번 단계에서는 평문 저장 (개발/학습 목적)
 *  - 도전 Lv6에서 BCrypt 등으로 암호화 예정
 *  - 응답 DTO/로그에 password를 노출하지 않도록 주의
 */

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 유저명 (필수, 최대 30자) */
    @Column(nullable = false, length = 30)
    private String username;

    /** 이메일 (필수, 최대 100자, UNIQUE) */
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    /** 비밀번호 (필수, 최대30자, 도전에서 암호화) */
    @Column(nullable = false, length = 100)
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 필요한 부분만 업데이트
    public void updateUsername(String username) {
        this.username = username;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    /** 비밀번호 변경(추가 요구 시 사용) — Lv6에서 인코더 적용 예정 */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
