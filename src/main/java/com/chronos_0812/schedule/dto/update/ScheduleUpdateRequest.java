package com.chronos_0812.schedule.dto.update;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 일정 수정 요청 DTO */

@Getter
@NoArgsConstructor                          // JSON 역직렬화(빈 생성자) 지원
public class ScheduleUpdateRequest {        ///  수정 요청

    @Nullable                               // 부분 업데이트를 할 수 있다는 의도를 표현으로 가능.
    @Size(max= 30)
    private String title;

    @Nullable
    @Size(max = 200)
    private String content;
}
