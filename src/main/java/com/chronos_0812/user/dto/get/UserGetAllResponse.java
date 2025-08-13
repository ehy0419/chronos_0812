package com.chronos_0812.user.dto.get;

import com.chronos_0812.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 유저 목록 조회 응답 DTO
 */
@Getter
@Builder

public class UserGetAllResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 정적 팩토리 from
    public static UserGetAllResponse from(User user) {
        return UserGetAllResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();                           // ← 최종 객체 반환
    }
}
