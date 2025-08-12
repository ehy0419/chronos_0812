package com.chronos_0812.schedule.dto.get;

import com.chronos_0812.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 목록 조회 응답 DTO
 * (필요 필드만 노출)
 */

@Getter
public class ScheduleGetAllResponse {       ///  전체 조회 응답

    private final Long id;
    private final String title;
    private final String content;
    private final String authorName;            // <- User 엔티티 대신 '작성 유저명'만 노출
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleGetAllResponse(
            Long id,
            String title,
            String content,
            String authorName,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // 정적 팩토리 : 엔티티 -> DTO 매핑 팩토리
    public static ScheduleGetAllResponse from(Schedule schedule) {
        return new ScheduleGetAllResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor().getUsername(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}
