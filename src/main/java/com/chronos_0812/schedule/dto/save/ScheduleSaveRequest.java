package com.chronos_0812.schedule.dto.save;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleSaveRequest {

    @NotBlank
    @Size(max = 30)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String content;

    @NotNull
    private Long userId;
    // 수정 private User author; -> Long userId;
    // 수정 사유 : 작성자 식별자만 받기 (엔티티 직접 의존 제거)

}
