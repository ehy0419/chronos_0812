package com.chronos_0812.schedule.dto.get;

import com.chronos_0812.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 단건 조회 응답 DTO
 * Lv2. userId, username 수정
 */

@Getter
public class ScheduleGetOneResponse {       /// 단건 조회 응답

    private final Long id;
    private final Long userId;              // Lv2. 추가
    private final String username;          // Lv2. 추가 <- User 엔티티 대신 '작성 유저명'만 노출
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;      // BaseEntity에서 상속
    private final LocalDateTime modifiedAt;     // BaseEntity에서 상속

    /**
     * private final User author;
     * 유저 (작성자) author를 그대로 노출시지키 않고 DTO에 작성자의 이름만 남기자.
     */

    public ScheduleGetOneResponse(
            Long id,
            Long userId,
            String username,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // 정적 팩토리: 엔티티 -> DTO 매핑
    // 수정 전(버그): (id, userId, title, content, username, ...)
//    public static ScheduleGetOneResponse from(Schedule schedule) {
//        return new ScheduleGetOneResponse(
//                schedule.getId(),
//                schedule.getUser().getId(),             // User user 연관관계
//                schedule.getTitle(),
//                schedule.getContent(),
//                schedule.getUser().getUsername(),       // User user 연관관계
//                schedule.getCreatedAt(),
//                schedule.getModifiedAt()
//        );
//    }

    // 수정 후(정상): (id, userId, username, title, content, ...)
    public static ScheduleGetOneResponse from(Schedule schedule) {
        return new ScheduleGetOneResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getUsername(), // ← 위치 중요!
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}

//오류 : schedule.getUsername(),
//오류문 : 'Schedule'의 메서드 'getUsername'을(를) 해결할 수 없습니다
//오류원인,해결 : Schedule에 String username이 아니라 User user 연관관계라서, DTO 매핑에서 schedule.getUsername() → schedule.getUser().getUsername() 로 바꿔주면 됩니다