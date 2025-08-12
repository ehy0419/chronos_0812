package com.chronos_0812.schedule.dto.save;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일정 생성 요청 DTO
 * (Lv1에서는 간단하게 필드만. Validation은 Lv5에서 확장)
 */

/**
 * 일정 생성 요청 DTO
 * Lv2: Long userId로 작성자 지정)
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
/**
 * 필드 검증
 * 컨트롤러의 @Valid는 요청 DTO(ScheduleSaveRequest)에 붙여서 필드 검증합니다.
 * 엔티티 쪽은 DB 스키마 제약(@Column(nullable, length))으로만 보완하고,
 * 문자열 길이/공백 검사는 DTO에 @NotBlank, @Size로 합니다.
 */