package com.chronos_0812.user.entity;

import com.chronos_0812.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)                  // 유저명은 필수, 최대 30자
    private String username;

    @Column(nullable = false, length = 100, unique = true)  // 이메일은 필수, 최대 30자
    private String email;

    @Column(nullable = false, length = 100)                 // 비밀번호는 필수, 최대 30자
    private String password;                            // 도전: 비밀번호 암호화
}
