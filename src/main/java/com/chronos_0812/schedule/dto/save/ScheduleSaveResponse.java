package com.chronos_0812.schedule.dto.save;

import com.chronos_0812.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleSaveResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final User author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleSaveResponse(
            Long id,
            String title,
            String content,
            User author,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
