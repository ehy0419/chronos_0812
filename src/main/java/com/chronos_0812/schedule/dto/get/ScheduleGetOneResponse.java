package com.chronos_0812.schedule.dto.get;

import com.chronos_0812.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleGetOneResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String authorName;            // <- User 엔티티 대신 '작성 유저명'만 노출
    private final LocalDateTime createdAt;      // BaseEntity에서 상속
    private final LocalDateTime modifiedAt;     // BaseEntity에서 상속

    /**
     * private final User author;
     * 유저 (작성자) author를 그대로 노출시지키 않고 DTO에 작성자의 이름만 남기자.
     */


    public ScheduleGetOneResponse(
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

    // 정적 팩토리: 엔티티 -> DTO 매핑
    public static ScheduleGetOneResponse from(Schedule schedule) {
        return new ScheduleGetOneResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor().getUsername(),         // 혹은 getName() 등 실제 필드명, 여기서 User.username 사용
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}
