package com.chronos_0812.user.dto.save;

import com.chronos_0812.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

/** 유저 생성 응답 DTO
 * 비밀번호 미노출 - 절대 미노출
 */

@Getter

public class UserSaveResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
                                                // 비밀번호 없는 것 확인

    public UserSaveResponse(
            Long id,
            String username,
            String email,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // 정적 메서드 from 사용
    public static UserSaveResponse from(User user) {
        return new UserSaveResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
