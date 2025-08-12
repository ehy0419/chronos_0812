package com.chronos_0812.schedule.dto.save;

import com.chronos_0812.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleSaveResponse {

    private final Long id;
    private final String title;
    private final String content;

    // 수정 : private final User author;
    // 수정 사유 : 엔티티 대신 식별자/표시용 필드만
    // 저장은 "누가? 무엇을? 언제 저장했는지? 만 알면 충분할 것이라 생각되서,
    // User 엔티티 대신에 authorId, authorName으로 바꾸면 좋겠다.
    private final Long authorId;
    private final String authorName;

    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleSaveResponse(
            Long id,
            String title,
            String content,
            Long authorId,
            String authorName,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // 정적 팩토리 : 엔티티 -> DTO
    public static ScheduleSaveResponse from(Schedule schedule) {
        return new ScheduleSaveResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor().getId(),
                schedule.getAuthor().getUsername(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}
