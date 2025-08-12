package com.chronos_0812.schedule.dto.get;

import com.chronos_0812.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleGetAllResponse {

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
}
