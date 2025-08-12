package com.chronos_0812.schedule.dto.save;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일정 생성 요청 DTO
 * (Lv1에서는 간단하게 필드만. Validation은 Lv5에서 확장)
 */

@Getter
@NoArgsConstructor
public class ScheduleSaveRequest {      ///  등록 요청

    @NotBlank
    @Size(max = 30)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String content;

    @NotNull
    private Long userId;
    // 수정 전 : private String author; / private User author;
    // 수정 후 : private Long userId;
    // 수정 사유 : 작성자 식별자만 받기 (엔티티 직접 의존 제거)

}
