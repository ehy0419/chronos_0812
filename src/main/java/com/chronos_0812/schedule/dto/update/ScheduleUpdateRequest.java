package com.chronos_0812.schedule.dto.update;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleUpdateRequest {

    @Nullable               // 부분 업데이트를 할 수 있다는 의도를 표현으로 가능.
    @Size(max= 30)
    private String title;

    @Nullable
    @Size(max = 200)
    private String content;

    public ScheduleUpdateRequest(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}
