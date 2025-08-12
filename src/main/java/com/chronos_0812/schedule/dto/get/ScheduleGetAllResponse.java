package com.chronos_0812.schedule.dto.get;

import com.chronos_0812.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 목록 조회 응답 DTO
 * (필요 필드만 노출)
 */

/**
 * 단건 조회 응답 DTO
 * Lv2. userId, username 수정
 */

@Getter
public class ScheduleGetAllResponse {       ///  전체 조회 응답

    private final Long id;
    private final Long userId;              // Lv2. 추가
    private final String username;          // Lv2. 추가 <- User 엔티티 대신 '작성 유저명'만 노출
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleGetAllResponse(
            Long id,
            Long userId,
            String username,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // 정적 팩토리 : 엔티티 -> DTO 매핑 팩토리
    public static ScheduleGetAllResponse from(Schedule schedule) {
        return new ScheduleGetAllResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getUsername(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}
